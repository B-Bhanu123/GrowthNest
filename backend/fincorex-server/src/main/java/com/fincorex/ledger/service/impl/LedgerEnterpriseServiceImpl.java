package com.fincorex.ledger.service.impl;

import com.fincorex.ledger.dto.LedgerDTO;
import com.fincorex.ledger.dto.CreateLedgerRequest;
import com.fincorex.ledger.entity.LedgerRecordEntity;
import com.fincorex.ledger.exception.LedgerDomainException;
import com.fincorex.ledger.repository.LedgerRepository;
import com.fincorex.ledger.service.LedgerService;
import com.fincorex.ledger.event.LedgerEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Enterprise Service Implementation for Double-Entry Financial Ledger
 * Implements strict ACID transactional isolation, idempotency validation, and event publishing.
 */
@Service("ledgerEnterpriseServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class LedgerEnterpriseServiceImpl extends LedgerService {

    private static final Logger log = LoggerFactory.getLogger(LedgerEnterpriseServiceImpl.class);

    private final LedgerRepository repository;
    private final LedgerEventHandler eventHandler;

    @Autowired
    public LedgerEnterpriseServiceImpl(LedgerRepository repository, LedgerEventHandler eventHandler) {
        super(repository);
        this.repository = repository;
        this.eventHandler = eventHandler;
    }

    public LedgerDTO executeEnterpriseOperation(CreateLedgerRequest request) {
        log.info("[ENTERPRISE-SERVICE] Processing operation for Double-Entry Financial Ledger - Ref: {}, Amount: {}",
                request.getReferenceCode(), request.getAmount());

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new LedgerDomainException("LEDGER_INVALID_AMOUNT", "Amount cannot be negative");
        }

        Optional<LedgerRecordEntity> existing = repository.findByReferenceCode(request.getReferenceCode());
        if (existing.isPresent()) {
            log.warn("[IDEMPOTENCY-CHECK] Duplicate operation attempt detected for ref: {}", request.getReferenceCode());
            return mapToDTO(existing.get());
        }

        LedgerRecordEntity entity = new LedgerRecordEntity(
                request.getReferenceCode(),
                request.getOwnerId(),
                request.getAmount(),
                "ACTIVE"
        );
        entity.setMetadataJson("{\"category\": \"" + request.getCategory() + "\", \"environment\": \"PRODUCTION\"}");

        LedgerRecordEntity saved = repository.save(entity);
        LedgerDTO dto = mapToDTO(saved);

        // Publish Event to Kafka Cluster
        eventHandler.publishLedgerCreatedEvent(dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<LedgerDTO> findRecordsPaged(UUID ownerId, Pageable pageable) {
        log.debug("[READ-ONLY] Fetching paged records for owner: {}", ownerId);
        return repository.findAll(pageable).map(this::mapToDTO);
    }

    public LedgerDTO archiveRecord(UUID id) {
        LedgerRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new LedgerDomainException("LEDGER_NOT_FOUND", "Record ID not found: " + id));
        entity.setStatus("ARCHIVED");
        entity.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(repository.save(entity));
    }

    private LedgerDTO mapToDTO(LedgerRecordEntity entity) {
        return new LedgerDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
