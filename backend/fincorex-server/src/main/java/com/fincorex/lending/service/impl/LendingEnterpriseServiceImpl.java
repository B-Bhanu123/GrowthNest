package com.fincorex.lending.service.impl;

import com.fincorex.lending.dto.LendingDTO;
import com.fincorex.lending.dto.CreateLendingRequest;
import com.fincorex.lending.entity.LendingRecordEntity;
import com.fincorex.lending.exception.LendingDomainException;
import com.fincorex.lending.repository.LendingRepository;
import com.fincorex.lending.service.LendingService;
import com.fincorex.lending.event.LendingEventHandler;
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
 * Enterprise Service Implementation for Lending & Underwriting Engine
 * Implements strict ACID transactional isolation, idempotency validation, and event publishing.
 */
@Service("lendingEnterpriseServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class LendingEnterpriseServiceImpl extends LendingService {

    private static final Logger log = LoggerFactory.getLogger(LendingEnterpriseServiceImpl.class);

    private final LendingRepository repository;
    private final LendingEventHandler eventHandler;

    @Autowired
    public LendingEnterpriseServiceImpl(LendingRepository repository, LendingEventHandler eventHandler) {
        super(repository);
        this.repository = repository;
        this.eventHandler = eventHandler;
    }

    public LendingDTO executeEnterpriseOperation(CreateLendingRequest request) {
        log.info("[ENTERPRISE-SERVICE] Processing operation for Lending & Underwriting Engine - Ref: {}, Amount: {}",
                request.getReferenceCode(), request.getAmount());

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new LendingDomainException("LENDING_INVALID_AMOUNT", "Amount cannot be negative");
        }

        Optional<LendingRecordEntity> existing = repository.findByReferenceCode(request.getReferenceCode());
        if (existing.isPresent()) {
            log.warn("[IDEMPOTENCY-CHECK] Duplicate operation attempt detected for ref: {}", request.getReferenceCode());
            return mapToDTO(existing.get());
        }

        LendingRecordEntity entity = new LendingRecordEntity(
                request.getReferenceCode(),
                request.getOwnerId(),
                request.getAmount(),
                "ACTIVE"
        );
        entity.setMetadataJson("{\"category\": \"" + request.getCategory() + "\", \"environment\": \"PRODUCTION\"}");

        LendingRecordEntity saved = repository.save(entity);
        LendingDTO dto = mapToDTO(saved);

        // Publish Event to Kafka Cluster
        eventHandler.publishLendingCreatedEvent(dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<LendingDTO> findRecordsPaged(UUID ownerId, Pageable pageable) {
        log.debug("[READ-ONLY] Fetching paged records for owner: {}", ownerId);
        return repository.findAll(pageable).map(this::mapToDTO);
    }

    public LendingDTO archiveRecord(UUID id) {
        LendingRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new LendingDomainException("LENDING_NOT_FOUND", "Record ID not found: " + id));
        entity.setStatus("ARCHIVED");
        entity.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(repository.save(entity));
    }

    private LendingDTO mapToDTO(LendingRecordEntity entity) {
        return new LendingDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
