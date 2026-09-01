package com.fincorex.admin.service.impl;

import com.fincorex.admin.dto.AdminDTO;
import com.fincorex.admin.dto.CreateAdminRequest;
import com.fincorex.admin.entity.AdminRecordEntity;
import com.fincorex.admin.exception.AdminDomainException;
import com.fincorex.admin.repository.AdminRepository;
import com.fincorex.admin.service.AdminService;
import com.fincorex.admin.event.AdminEventHandler;
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
 * Enterprise Service Implementation for Admin & Operations Center
 * Implements strict ACID transactional isolation, idempotency validation, and event publishing.
 */
@Service("adminEnterpriseServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class AdminEnterpriseServiceImpl extends AdminService {

    private static final Logger log = LoggerFactory.getLogger(AdminEnterpriseServiceImpl.class);

    private final AdminRepository repository;
    private final AdminEventHandler eventHandler;

    @Autowired
    public AdminEnterpriseServiceImpl(AdminRepository repository, AdminEventHandler eventHandler) {
        super(repository);
        this.repository = repository;
        this.eventHandler = eventHandler;
    }

    public AdminDTO executeEnterpriseOperation(CreateAdminRequest request) {
        log.info("[ENTERPRISE-SERVICE] Processing operation for Admin & Operations Center - Ref: {}, Amount: {}",
                request.getReferenceCode(), request.getAmount());

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new AdminDomainException("ADMIN_INVALID_AMOUNT", "Amount cannot be negative");
        }

        Optional<AdminRecordEntity> existing = repository.findByReferenceCode(request.getReferenceCode());
        if (existing.isPresent()) {
            log.warn("[IDEMPOTENCY-CHECK] Duplicate operation attempt detected for ref: {}", request.getReferenceCode());
            return mapToDTO(existing.get());
        }

        AdminRecordEntity entity = new AdminRecordEntity(
                request.getReferenceCode(),
                request.getOwnerId(),
                request.getAmount(),
                "ACTIVE"
        );
        entity.setMetadataJson("{\"category\": \"" + request.getCategory() + "\", \"environment\": \"PRODUCTION\"}");

        AdminRecordEntity saved = repository.save(entity);
        AdminDTO dto = mapToDTO(saved);

        // Publish Event to Kafka Cluster
        eventHandler.publishAdminCreatedEvent(dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<AdminDTO> findRecordsPaged(UUID ownerId, Pageable pageable) {
        log.debug("[READ-ONLY] Fetching paged records for owner: {}", ownerId);
        return repository.findAll(pageable).map(this::mapToDTO);
    }

    public AdminDTO archiveRecord(UUID id) {
        AdminRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new AdminDomainException("ADMIN_NOT_FOUND", "Record ID not found: " + id));
        entity.setStatus("ARCHIVED");
        entity.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(repository.save(entity));
    }

    private AdminDTO mapToDTO(AdminRecordEntity entity) {
        return new AdminDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
