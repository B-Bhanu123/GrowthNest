package com.fincorex.credit.service.impl;

import com.fincorex.credit.dto.CreditDTO;
import com.fincorex.credit.dto.CreateCreditRequest;
import com.fincorex.credit.entity.CreditRecordEntity;
import com.fincorex.credit.exception.CreditDomainException;
import com.fincorex.credit.repository.CreditRepository;
import com.fincorex.credit.service.CreditService;
import com.fincorex.credit.event.CreditEventHandler;
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
 * Enterprise Service Implementation for Credit Scoring System
 * Implements strict ACID transactional isolation, idempotency validation, and event publishing.
 */
@Service("creditEnterpriseServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class CreditEnterpriseServiceImpl extends CreditService {

    private static final Logger log = LoggerFactory.getLogger(CreditEnterpriseServiceImpl.class);

    private final CreditRepository repository;
    private final CreditEventHandler eventHandler;

    @Autowired
    public CreditEnterpriseServiceImpl(CreditRepository repository, CreditEventHandler eventHandler) {
        super(repository);
        this.repository = repository;
        this.eventHandler = eventHandler;
    }

    public CreditDTO executeEnterpriseOperation(CreateCreditRequest request) {
        log.info("[ENTERPRISE-SERVICE] Processing operation for Credit Scoring System - Ref: {}, Amount: {}",
                request.getReferenceCode(), request.getAmount());

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new CreditDomainException("CREDIT_INVALID_AMOUNT", "Amount cannot be negative");
        }

        Optional<CreditRecordEntity> existing = repository.findByReferenceCode(request.getReferenceCode());
        if (existing.isPresent()) {
            log.warn("[IDEMPOTENCY-CHECK] Duplicate operation attempt detected for ref: {}", request.getReferenceCode());
            return mapToDTO(existing.get());
        }

        CreditRecordEntity entity = new CreditRecordEntity(
                request.getReferenceCode(),
                request.getOwnerId(),
                request.getAmount(),
                "ACTIVE"
        );
        entity.setMetadataJson("{\"category\": \"" + request.getCategory() + "\", \"environment\": \"PRODUCTION\"}");

        CreditRecordEntity saved = repository.save(entity);
        CreditDTO dto = mapToDTO(saved);

        // Publish Event to Kafka Cluster
        eventHandler.publishCreditCreatedEvent(dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<CreditDTO> findRecordsPaged(UUID ownerId, Pageable pageable) {
        log.debug("[READ-ONLY] Fetching paged records for owner: {}", ownerId);
        return repository.findAll(pageable).map(this::mapToDTO);
    }

    public CreditDTO archiveRecord(UUID id) {
        CreditRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new CreditDomainException("CREDIT_NOT_FOUND", "Record ID not found: " + id));
        entity.setStatus("ARCHIVED");
        entity.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(repository.save(entity));
    }

    private CreditDTO mapToDTO(CreditRecordEntity entity) {
        return new CreditDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
