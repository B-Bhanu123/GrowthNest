package com.fincorex.settlement.service.impl;

import com.fincorex.settlement.dto.SettlementDTO;
import com.fincorex.settlement.dto.CreateSettlementRequest;
import com.fincorex.settlement.entity.SettlementRecordEntity;
import com.fincorex.settlement.exception.SettlementDomainException;
import com.fincorex.settlement.repository.SettlementRepository;
import com.fincorex.settlement.service.SettlementService;
import com.fincorex.settlement.event.SettlementEventHandler;
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
 * Enterprise Service Implementation for Merchant Batch Settlement
 * Implements strict ACID transactional isolation, idempotency validation, and event publishing.
 */
@Service("settlementEnterpriseServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class SettlementEnterpriseServiceImpl extends SettlementService {

    private static final Logger log = LoggerFactory.getLogger(SettlementEnterpriseServiceImpl.class);

    private final SettlementRepository repository;
    private final SettlementEventHandler eventHandler;

    @Autowired
    public SettlementEnterpriseServiceImpl(SettlementRepository repository, SettlementEventHandler eventHandler) {
        super(repository);
        this.repository = repository;
        this.eventHandler = eventHandler;
    }

    public SettlementDTO executeEnterpriseOperation(CreateSettlementRequest request) {
        log.info("[ENTERPRISE-SERVICE] Processing operation for Merchant Batch Settlement - Ref: {}, Amount: {}",
                request.getReferenceCode(), request.getAmount());

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new SettlementDomainException("SETTLEMENT_INVALID_AMOUNT", "Amount cannot be negative");
        }

        Optional<SettlementRecordEntity> existing = repository.findByReferenceCode(request.getReferenceCode());
        if (existing.isPresent()) {
            log.warn("[IDEMPOTENCY-CHECK] Duplicate operation attempt detected for ref: {}", request.getReferenceCode());
            return mapToDTO(existing.get());
        }

        SettlementRecordEntity entity = new SettlementRecordEntity(
                request.getReferenceCode(),
                request.getOwnerId(),
                request.getAmount(),
                "ACTIVE"
        );
        entity.setMetadataJson("{\"category\": \"" + request.getCategory() + "\", \"environment\": \"PRODUCTION\"}");

        SettlementRecordEntity saved = repository.save(entity);
        SettlementDTO dto = mapToDTO(saved);

        // Publish Event to Kafka Cluster
        eventHandler.publishSettlementCreatedEvent(dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<SettlementDTO> findRecordsPaged(UUID ownerId, Pageable pageable) {
        log.debug("[READ-ONLY] Fetching paged records for owner: {}", ownerId);
        return repository.findAll(pageable).map(this::mapToDTO);
    }

    public SettlementDTO archiveRecord(UUID id) {
        SettlementRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new SettlementDomainException("SETTLEMENT_NOT_FOUND", "Record ID not found: " + id));
        entity.setStatus("ARCHIVED");
        entity.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(repository.save(entity));
    }

    private SettlementDTO mapToDTO(SettlementRecordEntity entity) {
        return new SettlementDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
