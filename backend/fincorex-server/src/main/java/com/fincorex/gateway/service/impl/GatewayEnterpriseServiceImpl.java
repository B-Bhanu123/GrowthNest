package com.fincorex.gateway.service.impl;

import com.fincorex.gateway.dto.GatewayDTO;
import com.fincorex.gateway.dto.CreateGatewayRequest;
import com.fincorex.gateway.entity.GatewayRecordEntity;
import com.fincorex.gateway.exception.GatewayDomainException;
import com.fincorex.gateway.repository.GatewayRepository;
import com.fincorex.gateway.service.GatewayService;
import com.fincorex.gateway.event.GatewayEventHandler;
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
 * Enterprise Service Implementation for API Gateway & Security Proxy
 * Implements strict ACID transactional isolation, idempotency validation, and event publishing.
 */
@Service("gatewayEnterpriseServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class GatewayEnterpriseServiceImpl extends GatewayService {

    private static final Logger log = LoggerFactory.getLogger(GatewayEnterpriseServiceImpl.class);

    private final GatewayRepository repository;
    private final GatewayEventHandler eventHandler;

    @Autowired
    public GatewayEnterpriseServiceImpl(GatewayRepository repository, GatewayEventHandler eventHandler) {
        super(repository);
        this.repository = repository;
        this.eventHandler = eventHandler;
    }

    public GatewayDTO executeEnterpriseOperation(CreateGatewayRequest request) {
        log.info("[ENTERPRISE-SERVICE] Processing operation for API Gateway & Security Proxy - Ref: {}, Amount: {}",
                request.getReferenceCode(), request.getAmount());

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new GatewayDomainException("GATEWAY_INVALID_AMOUNT", "Amount cannot be negative");
        }

        Optional<GatewayRecordEntity> existing = repository.findByReferenceCode(request.getReferenceCode());
        if (existing.isPresent()) {
            log.warn("[IDEMPOTENCY-CHECK] Duplicate operation attempt detected for ref: {}", request.getReferenceCode());
            return mapToDTO(existing.get());
        }

        GatewayRecordEntity entity = new GatewayRecordEntity(
                request.getReferenceCode(),
                request.getOwnerId(),
                request.getAmount(),
                "ACTIVE"
        );
        entity.setMetadataJson("{\"category\": \"" + request.getCategory() + "\", \"environment\": \"PRODUCTION\"}");

        GatewayRecordEntity saved = repository.save(entity);
        GatewayDTO dto = mapToDTO(saved);

        // Publish Event to Kafka Cluster
        eventHandler.publishGatewayCreatedEvent(dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<GatewayDTO> findRecordsPaged(UUID ownerId, Pageable pageable) {
        log.debug("[READ-ONLY] Fetching paged records for owner: {}", ownerId);
        return repository.findAll(pageable).map(this::mapToDTO);
    }

    public GatewayDTO archiveRecord(UUID id) {
        GatewayRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new GatewayDomainException("GATEWAY_NOT_FOUND", "Record ID not found: " + id));
        entity.setStatus("ARCHIVED");
        entity.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(repository.save(entity));
    }

    private GatewayDTO mapToDTO(GatewayRecordEntity entity) {
        return new GatewayDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
