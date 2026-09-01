package com.fincorex.dispute.service.impl;

import com.fincorex.dispute.dto.DisputeDTO;
import com.fincorex.dispute.dto.CreateDisputeRequest;
import com.fincorex.dispute.entity.DisputeRecordEntity;
import com.fincorex.dispute.exception.DisputeDomainException;
import com.fincorex.dispute.repository.DisputeRepository;
import com.fincorex.dispute.service.DisputeService;
import com.fincorex.dispute.event.DisputeEventHandler;
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
 * Enterprise Service Implementation for Dispute & Chargeback Handling
 * Implements strict ACID transactional isolation, idempotency validation, and event publishing.
 */
@Service("disputeEnterpriseServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class DisputeEnterpriseServiceImpl extends DisputeService {

    private static final Logger log = LoggerFactory.getLogger(DisputeEnterpriseServiceImpl.class);

    private final DisputeRepository repository;
    private final DisputeEventHandler eventHandler;

    @Autowired
    public DisputeEnterpriseServiceImpl(DisputeRepository repository, DisputeEventHandler eventHandler) {
        super(repository);
        this.repository = repository;
        this.eventHandler = eventHandler;
    }

    public DisputeDTO executeEnterpriseOperation(CreateDisputeRequest request) {
        log.info("[ENTERPRISE-SERVICE] Processing operation for Dispute & Chargeback Handling - Ref: {}, Amount: {}",
                request.getReferenceCode(), request.getAmount());

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new DisputeDomainException("DISPUTE_INVALID_AMOUNT", "Amount cannot be negative");
        }

        Optional<DisputeRecordEntity> existing = repository.findByReferenceCode(request.getReferenceCode());
        if (existing.isPresent()) {
            log.warn("[IDEMPOTENCY-CHECK] Duplicate operation attempt detected for ref: {}", request.getReferenceCode());
            return mapToDTO(existing.get());
        }

        DisputeRecordEntity entity = new DisputeRecordEntity(
                request.getReferenceCode(),
                request.getOwnerId(),
                request.getAmount(),
                "ACTIVE"
        );
        entity.setMetadataJson("{\"category\": \"" + request.getCategory() + "\", \"environment\": \"PRODUCTION\"}");

        DisputeRecordEntity saved = repository.save(entity);
        DisputeDTO dto = mapToDTO(saved);

        // Publish Event to Kafka Cluster
        eventHandler.publishDisputeCreatedEvent(dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<DisputeDTO> findRecordsPaged(UUID ownerId, Pageable pageable) {
        log.debug("[READ-ONLY] Fetching paged records for owner: {}", ownerId);
        return repository.findAll(pageable).map(this::mapToDTO);
    }

    public DisputeDTO archiveRecord(UUID id) {
        DisputeRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new DisputeDomainException("DISPUTE_NOT_FOUND", "Record ID not found: " + id));
        entity.setStatus("ARCHIVED");
        entity.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(repository.save(entity));
    }

    private DisputeDTO mapToDTO(DisputeRecordEntity entity) {
        return new DisputeDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
