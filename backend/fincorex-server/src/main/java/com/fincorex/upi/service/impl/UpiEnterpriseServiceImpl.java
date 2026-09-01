package com.fincorex.upi.service.impl;

import com.fincorex.upi.dto.UpiDTO;
import com.fincorex.upi.dto.CreateUpiRequest;
import com.fincorex.upi.entity.UpiRecordEntity;
import com.fincorex.upi.exception.UpiDomainException;
import com.fincorex.upi.repository.UpiRepository;
import com.fincorex.upi.service.UpiService;
import com.fincorex.upi.event.UpiEventHandler;
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
 * Enterprise Service Implementation for UPI Instant Transfer Network
 * Implements strict ACID transactional isolation, idempotency validation, and event publishing.
 */
@Service("upiEnterpriseServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class UpiEnterpriseServiceImpl extends UpiService {

    private static final Logger log = LoggerFactory.getLogger(UpiEnterpriseServiceImpl.class);

    private final UpiRepository repository;
    private final UpiEventHandler eventHandler;

    @Autowired
    public UpiEnterpriseServiceImpl(UpiRepository repository, UpiEventHandler eventHandler) {
        super(repository);
        this.repository = repository;
        this.eventHandler = eventHandler;
    }

    public UpiDTO executeEnterpriseOperation(CreateUpiRequest request) {
        log.info("[ENTERPRISE-SERVICE] Processing operation for UPI Instant Transfer Network - Ref: {}, Amount: {}",
                request.getReferenceCode(), request.getAmount());

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new UpiDomainException("UPI_INVALID_AMOUNT", "Amount cannot be negative");
        }

        Optional<UpiRecordEntity> existing = repository.findByReferenceCode(request.getReferenceCode());
        if (existing.isPresent()) {
            log.warn("[IDEMPOTENCY-CHECK] Duplicate operation attempt detected for ref: {}", request.getReferenceCode());
            return mapToDTO(existing.get());
        }

        UpiRecordEntity entity = new UpiRecordEntity(
                request.getReferenceCode(),
                request.getOwnerId(),
                request.getAmount(),
                "ACTIVE"
        );
        entity.setMetadataJson("{\"category\": \"" + request.getCategory() + "\", \"environment\": \"PRODUCTION\"}");

        UpiRecordEntity saved = repository.save(entity);
        UpiDTO dto = mapToDTO(saved);

        // Publish Event to Kafka Cluster
        eventHandler.publishUpiCreatedEvent(dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<UpiDTO> findRecordsPaged(UUID ownerId, Pageable pageable) {
        log.debug("[READ-ONLY] Fetching paged records for owner: {}", ownerId);
        return repository.findAll(pageable).map(this::mapToDTO);
    }

    public UpiDTO archiveRecord(UUID id) {
        UpiRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new UpiDomainException("UPI_NOT_FOUND", "Record ID not found: " + id));
        entity.setStatus("ARCHIVED");
        entity.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(repository.save(entity));
    }

    private UpiDTO mapToDTO(UpiRecordEntity entity) {
        return new UpiDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
