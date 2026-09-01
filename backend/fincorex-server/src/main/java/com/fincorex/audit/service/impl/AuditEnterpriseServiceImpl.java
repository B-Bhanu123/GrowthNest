package com.fincorex.audit.service.impl;

import com.fincorex.audit.dto.AuditDTO;
import com.fincorex.audit.dto.CreateAuditRequest;
import com.fincorex.audit.entity.AuditRecordEntity;
import com.fincorex.audit.exception.AuditDomainException;
import com.fincorex.audit.repository.AuditRepository;
import com.fincorex.audit.service.AuditService;
import com.fincorex.audit.event.AuditEventHandler;
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
 * Enterprise Service Implementation for Immutable Audit Logging
 * Implements strict ACID transactional isolation, idempotency validation, and event publishing.
 */
@Service("auditEnterpriseServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class AuditEnterpriseServiceImpl extends AuditService {

    private static final Logger log = LoggerFactory.getLogger(AuditEnterpriseServiceImpl.class);

    private final AuditRepository repository;
    private final AuditEventHandler eventHandler;

    @Autowired
    public AuditEnterpriseServiceImpl(AuditRepository repository, AuditEventHandler eventHandler) {
        super(repository);
        this.repository = repository;
        this.eventHandler = eventHandler;
    }

    public AuditDTO executeEnterpriseOperation(CreateAuditRequest request) {
        log.info("[ENTERPRISE-SERVICE] Processing operation for Immutable Audit Logging - Ref: {}, Amount: {}",
                request.getReferenceCode(), request.getAmount());

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new AuditDomainException("AUDIT_INVALID_AMOUNT", "Amount cannot be negative");
        }

        Optional<AuditRecordEntity> existing = repository.findByReferenceCode(request.getReferenceCode());
        if (existing.isPresent()) {
            log.warn("[IDEMPOTENCY-CHECK] Duplicate operation attempt detected for ref: {}", request.getReferenceCode());
            return mapToDTO(existing.get());
        }

        AuditRecordEntity entity = new AuditRecordEntity(
                request.getReferenceCode(),
                request.getOwnerId(),
                request.getAmount(),
                "ACTIVE"
        );
        entity.setMetadataJson("{\"category\": \"" + request.getCategory() + "\", \"environment\": \"PRODUCTION\"}");

        AuditRecordEntity saved = repository.save(entity);
        AuditDTO dto = mapToDTO(saved);

        // Publish Event to Kafka Cluster
        eventHandler.publishAuditCreatedEvent(dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<AuditDTO> findRecordsPaged(UUID ownerId, Pageable pageable) {
        log.debug("[READ-ONLY] Fetching paged records for owner: {}", ownerId);
        return repository.findAll(pageable).map(this::mapToDTO);
    }

    public AuditDTO archiveRecord(UUID id) {
        AuditRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new AuditDomainException("AUDIT_NOT_FOUND", "Record ID not found: " + id));
        entity.setStatus("ARCHIVED");
        entity.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(repository.save(entity));
    }

    private AuditDTO mapToDTO(AuditRecordEntity entity) {
        return new AuditDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
