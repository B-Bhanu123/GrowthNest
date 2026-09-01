package com.fincorex.notification.service.impl;

import com.fincorex.notification.dto.NotificationDTO;
import com.fincorex.notification.dto.CreateNotificationRequest;
import com.fincorex.notification.entity.NotificationRecordEntity;
import com.fincorex.notification.exception.NotificationDomainException;
import com.fincorex.notification.repository.NotificationRepository;
import com.fincorex.notification.service.NotificationService;
import com.fincorex.notification.event.NotificationEventHandler;
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
 * Enterprise Service Implementation for Centralized Notification System
 * Implements strict ACID transactional isolation, idempotency validation, and event publishing.
 */
@Service("notificationEnterpriseServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class NotificationEnterpriseServiceImpl extends NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationEnterpriseServiceImpl.class);

    private final NotificationRepository repository;
    private final NotificationEventHandler eventHandler;

    @Autowired
    public NotificationEnterpriseServiceImpl(NotificationRepository repository, NotificationEventHandler eventHandler) {
        super(repository);
        this.repository = repository;
        this.eventHandler = eventHandler;
    }

    public NotificationDTO executeEnterpriseOperation(CreateNotificationRequest request) {
        log.info("[ENTERPRISE-SERVICE] Processing operation for Centralized Notification System - Ref: {}, Amount: {}",
                request.getReferenceCode(), request.getAmount());

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new NotificationDomainException("NOTIFICATION_INVALID_AMOUNT", "Amount cannot be negative");
        }

        Optional<NotificationRecordEntity> existing = repository.findByReferenceCode(request.getReferenceCode());
        if (existing.isPresent()) {
            log.warn("[IDEMPOTENCY-CHECK] Duplicate operation attempt detected for ref: {}", request.getReferenceCode());
            return mapToDTO(existing.get());
        }

        NotificationRecordEntity entity = new NotificationRecordEntity(
                request.getReferenceCode(),
                request.getOwnerId(),
                request.getAmount(),
                "ACTIVE"
        );
        entity.setMetadataJson("{\"category\": \"" + request.getCategory() + "\", \"environment\": \"PRODUCTION\"}");

        NotificationRecordEntity saved = repository.save(entity);
        NotificationDTO dto = mapToDTO(saved);

        // Publish Event to Kafka Cluster
        eventHandler.publishNotificationCreatedEvent(dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<NotificationDTO> findRecordsPaged(UUID ownerId, Pageable pageable) {
        log.debug("[READ-ONLY] Fetching paged records for owner: {}", ownerId);
        return repository.findAll(pageable).map(this::mapToDTO);
    }

    public NotificationDTO archiveRecord(UUID id) {
        NotificationRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new NotificationDomainException("NOTIFICATION_NOT_FOUND", "Record ID not found: " + id));
        entity.setStatus("ARCHIVED");
        entity.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(repository.save(entity));
    }

    private NotificationDTO mapToDTO(NotificationRecordEntity entity) {
        return new NotificationDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
