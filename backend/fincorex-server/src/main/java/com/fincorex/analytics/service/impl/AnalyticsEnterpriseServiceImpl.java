package com.fincorex.analytics.service.impl;

import com.fincorex.analytics.dto.AnalyticsDTO;
import com.fincorex.analytics.dto.CreateAnalyticsRequest;
import com.fincorex.analytics.entity.AnalyticsRecordEntity;
import com.fincorex.analytics.exception.AnalyticsDomainException;
import com.fincorex.analytics.repository.AnalyticsRepository;
import com.fincorex.analytics.service.AnalyticsService;
import com.fincorex.analytics.event.AnalyticsEventHandler;
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
 * Enterprise Service Implementation for Financial Analytics Engine
 * Implements strict ACID transactional isolation, idempotency validation, and event publishing.
 */
@Service("analyticsEnterpriseServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class AnalyticsEnterpriseServiceImpl extends AnalyticsService {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsEnterpriseServiceImpl.class);

    private final AnalyticsRepository repository;
    private final AnalyticsEventHandler eventHandler;

    @Autowired
    public AnalyticsEnterpriseServiceImpl(AnalyticsRepository repository, AnalyticsEventHandler eventHandler) {
        super(repository);
        this.repository = repository;
        this.eventHandler = eventHandler;
    }

    public AnalyticsDTO executeEnterpriseOperation(CreateAnalyticsRequest request) {
        log.info("[ENTERPRISE-SERVICE] Processing operation for Financial Analytics Engine - Ref: {}, Amount: {}",
                request.getReferenceCode(), request.getAmount());

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new AnalyticsDomainException("ANALYTICS_INVALID_AMOUNT", "Amount cannot be negative");
        }

        Optional<AnalyticsRecordEntity> existing = repository.findByReferenceCode(request.getReferenceCode());
        if (existing.isPresent()) {
            log.warn("[IDEMPOTENCY-CHECK] Duplicate operation attempt detected for ref: {}", request.getReferenceCode());
            return mapToDTO(existing.get());
        }

        AnalyticsRecordEntity entity = new AnalyticsRecordEntity(
                request.getReferenceCode(),
                request.getOwnerId(),
                request.getAmount(),
                "ACTIVE"
        );
        entity.setMetadataJson("{\"category\": \"" + request.getCategory() + "\", \"environment\": \"PRODUCTION\"}");

        AnalyticsRecordEntity saved = repository.save(entity);
        AnalyticsDTO dto = mapToDTO(saved);

        // Publish Event to Kafka Cluster
        eventHandler.publishAnalyticsCreatedEvent(dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<AnalyticsDTO> findRecordsPaged(UUID ownerId, Pageable pageable) {
        log.debug("[READ-ONLY] Fetching paged records for owner: {}", ownerId);
        return repository.findAll(pageable).map(this::mapToDTO);
    }

    public AnalyticsDTO archiveRecord(UUID id) {
        AnalyticsRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new AnalyticsDomainException("ANALYTICS_NOT_FOUND", "Record ID not found: " + id));
        entity.setStatus("ARCHIVED");
        entity.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(repository.save(entity));
    }

    private AnalyticsDTO mapToDTO(AnalyticsRecordEntity entity) {
        return new AnalyticsDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
