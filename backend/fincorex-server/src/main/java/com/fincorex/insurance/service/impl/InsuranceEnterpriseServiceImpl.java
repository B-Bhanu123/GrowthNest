package com.fincorex.insurance.service.impl;

import com.fincorex.insurance.dto.InsuranceDTO;
import com.fincorex.insurance.dto.CreateInsuranceRequest;
import com.fincorex.insurance.entity.InsuranceRecordEntity;
import com.fincorex.insurance.exception.InsuranceDomainException;
import com.fincorex.insurance.repository.InsuranceRepository;
import com.fincorex.insurance.service.InsuranceService;
import com.fincorex.insurance.event.InsuranceEventHandler;
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
 * Enterprise Service Implementation for Insurance Policy System
 * Implements strict ACID transactional isolation, idempotency validation, and event publishing.
 */
@Service("insuranceEnterpriseServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class InsuranceEnterpriseServiceImpl extends InsuranceService {

    private static final Logger log = LoggerFactory.getLogger(InsuranceEnterpriseServiceImpl.class);

    private final InsuranceRepository repository;
    private final InsuranceEventHandler eventHandler;

    @Autowired
    public InsuranceEnterpriseServiceImpl(InsuranceRepository repository, InsuranceEventHandler eventHandler) {
        super(repository);
        this.repository = repository;
        this.eventHandler = eventHandler;
    }

    public InsuranceDTO executeEnterpriseOperation(CreateInsuranceRequest request) {
        log.info("[ENTERPRISE-SERVICE] Processing operation for Insurance Policy System - Ref: {}, Amount: {}",
                request.getReferenceCode(), request.getAmount());

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new InsuranceDomainException("INSURANCE_INVALID_AMOUNT", "Amount cannot be negative");
        }

        Optional<InsuranceRecordEntity> existing = repository.findByReferenceCode(request.getReferenceCode());
        if (existing.isPresent()) {
            log.warn("[IDEMPOTENCY-CHECK] Duplicate operation attempt detected for ref: {}", request.getReferenceCode());
            return mapToDTO(existing.get());
        }

        InsuranceRecordEntity entity = new InsuranceRecordEntity(
                request.getReferenceCode(),
                request.getOwnerId(),
                request.getAmount(),
                "ACTIVE"
        );
        entity.setMetadataJson("{\"category\": \"" + request.getCategory() + "\", \"environment\": \"PRODUCTION\"}");

        InsuranceRecordEntity saved = repository.save(entity);
        InsuranceDTO dto = mapToDTO(saved);

        // Publish Event to Kafka Cluster
        eventHandler.publishInsuranceCreatedEvent(dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<InsuranceDTO> findRecordsPaged(UUID ownerId, Pageable pageable) {
        log.debug("[READ-ONLY] Fetching paged records for owner: {}", ownerId);
        return repository.findAll(pageable).map(this::mapToDTO);
    }

    public InsuranceDTO archiveRecord(UUID id) {
        InsuranceRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new InsuranceDomainException("INSURANCE_NOT_FOUND", "Record ID not found: " + id));
        entity.setStatus("ARCHIVED");
        entity.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(repository.save(entity));
    }

    private InsuranceDTO mapToDTO(InsuranceRecordEntity entity) {
        return new InsuranceDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
