package com.fincorex.identity.service.impl;

import com.fincorex.identity.dto.IdentityDTO;
import com.fincorex.identity.dto.CreateIdentityRequest;
import com.fincorex.identity.entity.IdentityRecordEntity;
import com.fincorex.identity.exception.IdentityDomainException;
import com.fincorex.identity.repository.IdentityRepository;
import com.fincorex.identity.service.IdentityService;
import com.fincorex.identity.event.IdentityEventHandler;
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
 * Enterprise Service Implementation for Identity & Access Management
 * Implements strict ACID transactional isolation, idempotency validation, and event publishing.
 */
@Service("identityEnterpriseServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class IdentityEnterpriseServiceImpl extends IdentityService {

    private static final Logger log = LoggerFactory.getLogger(IdentityEnterpriseServiceImpl.class);

    private final IdentityRepository repository;
    private final IdentityEventHandler eventHandler;

    @Autowired
    public IdentityEnterpriseServiceImpl(IdentityRepository repository, IdentityEventHandler eventHandler) {
        super(repository);
        this.repository = repository;
        this.eventHandler = eventHandler;
    }

    public IdentityDTO executeEnterpriseOperation(CreateIdentityRequest request) {
        log.info("[ENTERPRISE-SERVICE] Processing operation for Identity & Access Management - Ref: {}, Amount: {}",
                request.getReferenceCode(), request.getAmount());

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new IdentityDomainException("IDENTITY_INVALID_AMOUNT", "Amount cannot be negative");
        }

        Optional<IdentityRecordEntity> existing = repository.findByReferenceCode(request.getReferenceCode());
        if (existing.isPresent()) {
            log.warn("[IDEMPOTENCY-CHECK] Duplicate operation attempt detected for ref: {}", request.getReferenceCode());
            return mapToDTO(existing.get());
        }

        IdentityRecordEntity entity = new IdentityRecordEntity(
                request.getReferenceCode(),
                request.getOwnerId(),
                request.getAmount(),
                "ACTIVE"
        );
        entity.setMetadataJson("{\"category\": \"" + request.getCategory() + "\", \"environment\": \"PRODUCTION\"}");

        IdentityRecordEntity saved = repository.save(entity);
        IdentityDTO dto = mapToDTO(saved);

        // Publish Event to Kafka Cluster
        eventHandler.publishIdentityCreatedEvent(dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<IdentityDTO> findRecordsPaged(UUID ownerId, Pageable pageable) {
        log.debug("[READ-ONLY] Fetching paged records for owner: {}", ownerId);
        return repository.findAll(pageable).map(this::mapToDTO);
    }

    public IdentityDTO archiveRecord(UUID id) {
        IdentityRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IdentityDomainException("IDENTITY_NOT_FOUND", "Record ID not found: " + id));
        entity.setStatus("ARCHIVED");
        entity.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(repository.save(entity));
    }

    private IdentityDTO mapToDTO(IdentityRecordEntity entity) {
        return new IdentityDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
