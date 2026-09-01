package com.fincorex.reconciliation.service.impl;

import com.fincorex.reconciliation.dto.ReconciliationDTO;
import com.fincorex.reconciliation.dto.CreateReconciliationRequest;
import com.fincorex.reconciliation.entity.ReconciliationRecordEntity;
import com.fincorex.reconciliation.exception.ReconciliationDomainException;
import com.fincorex.reconciliation.repository.ReconciliationRepository;
import com.fincorex.reconciliation.service.ReconciliationService;
import com.fincorex.reconciliation.event.ReconciliationEventHandler;
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
 * Enterprise Service Implementation for Automated Bank Reconciliation
 * Implements strict ACID transactional isolation, idempotency validation, and event publishing.
 */
@Service("reconciliationEnterpriseServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class ReconciliationEnterpriseServiceImpl extends ReconciliationService {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationEnterpriseServiceImpl.class);

    private final ReconciliationRepository repository;
    private final ReconciliationEventHandler eventHandler;

    @Autowired
    public ReconciliationEnterpriseServiceImpl(ReconciliationRepository repository, ReconciliationEventHandler eventHandler) {
        super(repository);
        this.repository = repository;
        this.eventHandler = eventHandler;
    }

    public ReconciliationDTO executeEnterpriseOperation(CreateReconciliationRequest request) {
        log.info("[ENTERPRISE-SERVICE] Processing operation for Automated Bank Reconciliation - Ref: {}, Amount: {}",
                request.getReferenceCode(), request.getAmount());

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new ReconciliationDomainException("RECONCILIATION_INVALID_AMOUNT", "Amount cannot be negative");
        }

        Optional<ReconciliationRecordEntity> existing = repository.findByReferenceCode(request.getReferenceCode());
        if (existing.isPresent()) {
            log.warn("[IDEMPOTENCY-CHECK] Duplicate operation attempt detected for ref: {}", request.getReferenceCode());
            return mapToDTO(existing.get());
        }

        ReconciliationRecordEntity entity = new ReconciliationRecordEntity(
                request.getReferenceCode(),
                request.getOwnerId(),
                request.getAmount(),
                "ACTIVE"
        );
        entity.setMetadataJson("{\"category\": \"" + request.getCategory() + "\", \"environment\": \"PRODUCTION\"}");

        ReconciliationRecordEntity saved = repository.save(entity);
        ReconciliationDTO dto = mapToDTO(saved);

        // Publish Event to Kafka Cluster
        eventHandler.publishReconciliationCreatedEvent(dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<ReconciliationDTO> findRecordsPaged(UUID ownerId, Pageable pageable) {
        log.debug("[READ-ONLY] Fetching paged records for owner: {}", ownerId);
        return repository.findAll(pageable).map(this::mapToDTO);
    }

    public ReconciliationDTO archiveRecord(UUID id) {
        ReconciliationRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new ReconciliationDomainException("RECONCILIATION_NOT_FOUND", "Record ID not found: " + id));
        entity.setStatus("ARCHIVED");
        entity.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(repository.save(entity));
    }

    private ReconciliationDTO mapToDTO(ReconciliationRecordEntity entity) {
        return new ReconciliationDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
