package com.fincorex.fraud.service.impl;

import com.fincorex.fraud.dto.FraudDTO;
import com.fincorex.fraud.dto.CreateFraudRequest;
import com.fincorex.fraud.entity.FraudRecordEntity;
import com.fincorex.fraud.exception.FraudDomainException;
import com.fincorex.fraud.repository.FraudRepository;
import com.fincorex.fraud.service.FraudService;
import com.fincorex.fraud.event.FraudEventHandler;
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
 * Enterprise Service Implementation for Real-Time Fraud Detection Engine
 * Implements strict ACID transactional isolation, idempotency validation, and event publishing.
 */
@Service("fraudEnterpriseServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class FraudEnterpriseServiceImpl extends FraudService {

    private static final Logger log = LoggerFactory.getLogger(FraudEnterpriseServiceImpl.class);

    private final FraudRepository repository;
    private final FraudEventHandler eventHandler;

    @Autowired
    public FraudEnterpriseServiceImpl(FraudRepository repository, FraudEventHandler eventHandler) {
        super(repository);
        this.repository = repository;
        this.eventHandler = eventHandler;
    }

    public FraudDTO executeEnterpriseOperation(CreateFraudRequest request) {
        log.info("[ENTERPRISE-SERVICE] Processing operation for Real-Time Fraud Detection Engine - Ref: {}, Amount: {}",
                request.getReferenceCode(), request.getAmount());

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new FraudDomainException("FRAUD_INVALID_AMOUNT", "Amount cannot be negative");
        }

        Optional<FraudRecordEntity> existing = repository.findByReferenceCode(request.getReferenceCode());
        if (existing.isPresent()) {
            log.warn("[IDEMPOTENCY-CHECK] Duplicate operation attempt detected for ref: {}", request.getReferenceCode());
            return mapToDTO(existing.get());
        }

        FraudRecordEntity entity = new FraudRecordEntity(
                request.getReferenceCode(),
                request.getOwnerId(),
                request.getAmount(),
                "ACTIVE"
        );
        entity.setMetadataJson("{\"category\": \"" + request.getCategory() + "\", \"environment\": \"PRODUCTION\"}");

        FraudRecordEntity saved = repository.save(entity);
        FraudDTO dto = mapToDTO(saved);

        // Publish Event to Kafka Cluster
        eventHandler.publishFraudCreatedEvent(dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<FraudDTO> findRecordsPaged(UUID ownerId, Pageable pageable) {
        log.debug("[READ-ONLY] Fetching paged records for owner: {}", ownerId);
        return repository.findAll(pageable).map(this::mapToDTO);
    }

    public FraudDTO archiveRecord(UUID id) {
        FraudRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new FraudDomainException("FRAUD_NOT_FOUND", "Record ID not found: " + id));
        entity.setStatus("ARCHIVED");
        entity.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(repository.save(entity));
    }

    private FraudDTO mapToDTO(FraudRecordEntity entity) {
        return new FraudDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
