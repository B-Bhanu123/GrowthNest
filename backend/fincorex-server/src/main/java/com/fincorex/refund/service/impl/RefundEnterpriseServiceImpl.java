package com.fincorex.refund.service.impl;

import com.fincorex.refund.dto.RefundDTO;
import com.fincorex.refund.dto.CreateRefundRequest;
import com.fincorex.refund.entity.RefundRecordEntity;
import com.fincorex.refund.exception.RefundDomainException;
import com.fincorex.refund.repository.RefundRepository;
import com.fincorex.refund.service.RefundService;
import com.fincorex.refund.event.RefundEventHandler;
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
 * Enterprise Service Implementation for Refund Management
 * Implements strict ACID transactional isolation, idempotency validation, and event publishing.
 */
@Service("refundEnterpriseServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class RefundEnterpriseServiceImpl extends RefundService {

    private static final Logger log = LoggerFactory.getLogger(RefundEnterpriseServiceImpl.class);

    private final RefundRepository repository;
    private final RefundEventHandler eventHandler;

    @Autowired
    public RefundEnterpriseServiceImpl(RefundRepository repository, RefundEventHandler eventHandler) {
        super(repository);
        this.repository = repository;
        this.eventHandler = eventHandler;
    }

    public RefundDTO executeEnterpriseOperation(CreateRefundRequest request) {
        log.info("[ENTERPRISE-SERVICE] Processing operation for Refund Management - Ref: {}, Amount: {}",
                request.getReferenceCode(), request.getAmount());

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new RefundDomainException("REFUND_INVALID_AMOUNT", "Amount cannot be negative");
        }

        Optional<RefundRecordEntity> existing = repository.findByReferenceCode(request.getReferenceCode());
        if (existing.isPresent()) {
            log.warn("[IDEMPOTENCY-CHECK] Duplicate operation attempt detected for ref: {}", request.getReferenceCode());
            return mapToDTO(existing.get());
        }

        RefundRecordEntity entity = new RefundRecordEntity(
                request.getReferenceCode(),
                request.getOwnerId(),
                request.getAmount(),
                "ACTIVE"
        );
        entity.setMetadataJson("{\"category\": \"" + request.getCategory() + "\", \"environment\": \"PRODUCTION\"}");

        RefundRecordEntity saved = repository.save(entity);
        RefundDTO dto = mapToDTO(saved);

        // Publish Event to Kafka Cluster
        eventHandler.publishRefundCreatedEvent(dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<RefundDTO> findRecordsPaged(UUID ownerId, Pageable pageable) {
        log.debug("[READ-ONLY] Fetching paged records for owner: {}", ownerId);
        return repository.findAll(pageable).map(this::mapToDTO);
    }

    public RefundDTO archiveRecord(UUID id) {
        RefundRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new RefundDomainException("REFUND_NOT_FOUND", "Record ID not found: " + id));
        entity.setStatus("ARCHIVED");
        entity.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(repository.save(entity));
    }

    private RefundDTO mapToDTO(RefundRecordEntity entity) {
        return new RefundDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
