package com.fincorex.merchant.service.impl;

import com.fincorex.merchant.dto.MerchantDTO;
import com.fincorex.merchant.dto.CreateMerchantRequest;
import com.fincorex.merchant.entity.MerchantRecordEntity;
import com.fincorex.merchant.exception.MerchantDomainException;
import com.fincorex.merchant.repository.MerchantRepository;
import com.fincorex.merchant.service.MerchantService;
import com.fincorex.merchant.event.MerchantEventHandler;
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
 * Enterprise Service Implementation for Merchant Acquiring Management
 * Implements strict ACID transactional isolation, idempotency validation, and event publishing.
 */
@Service("merchantEnterpriseServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class MerchantEnterpriseServiceImpl extends MerchantService {

    private static final Logger log = LoggerFactory.getLogger(MerchantEnterpriseServiceImpl.class);

    private final MerchantRepository repository;
    private final MerchantEventHandler eventHandler;

    @Autowired
    public MerchantEnterpriseServiceImpl(MerchantRepository repository, MerchantEventHandler eventHandler) {
        super(repository);
        this.repository = repository;
        this.eventHandler = eventHandler;
    }

    public MerchantDTO executeEnterpriseOperation(CreateMerchantRequest request) {
        log.info("[ENTERPRISE-SERVICE] Processing operation for Merchant Acquiring Management - Ref: {}, Amount: {}",
                request.getReferenceCode(), request.getAmount());

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new MerchantDomainException("MERCHANT_INVALID_AMOUNT", "Amount cannot be negative");
        }

        Optional<MerchantRecordEntity> existing = repository.findByReferenceCode(request.getReferenceCode());
        if (existing.isPresent()) {
            log.warn("[IDEMPOTENCY-CHECK] Duplicate operation attempt detected for ref: {}", request.getReferenceCode());
            return mapToDTO(existing.get());
        }

        MerchantRecordEntity entity = new MerchantRecordEntity(
                request.getReferenceCode(),
                request.getOwnerId(),
                request.getAmount(),
                "ACTIVE"
        );
        entity.setMetadataJson("{\"category\": \"" + request.getCategory() + "\", \"environment\": \"PRODUCTION\"}");

        MerchantRecordEntity saved = repository.save(entity);
        MerchantDTO dto = mapToDTO(saved);

        // Publish Event to Kafka Cluster
        eventHandler.publishMerchantCreatedEvent(dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<MerchantDTO> findRecordsPaged(UUID ownerId, Pageable pageable) {
        log.debug("[READ-ONLY] Fetching paged records for owner: {}", ownerId);
        return repository.findAll(pageable).map(this::mapToDTO);
    }

    public MerchantDTO archiveRecord(UUID id) {
        MerchantRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new MerchantDomainException("MERCHANT_NOT_FOUND", "Record ID not found: " + id));
        entity.setStatus("ARCHIVED");
        entity.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(repository.save(entity));
    }

    private MerchantDTO mapToDTO(MerchantRecordEntity entity) {
        return new MerchantDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
