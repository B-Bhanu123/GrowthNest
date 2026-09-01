package com.fincorex.customer.service.impl;

import com.fincorex.customer.dto.CustomerDTO;
import com.fincorex.customer.dto.CreateCustomerRequest;
import com.fincorex.customer.entity.CustomerRecordEntity;
import com.fincorex.customer.exception.CustomerDomainException;
import com.fincorex.customer.repository.CustomerRepository;
import com.fincorex.customer.service.CustomerService;
import com.fincorex.customer.event.CustomerEventHandler;
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
 * Enterprise Service Implementation for Customer & Account Management
 * Implements strict ACID transactional isolation, idempotency validation, and event publishing.
 */
@Service("customerEnterpriseServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class CustomerEnterpriseServiceImpl extends CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerEnterpriseServiceImpl.class);

    private final CustomerRepository repository;
    private final CustomerEventHandler eventHandler;

    @Autowired
    public CustomerEnterpriseServiceImpl(CustomerRepository repository, CustomerEventHandler eventHandler) {
        super(repository);
        this.repository = repository;
        this.eventHandler = eventHandler;
    }

    public CustomerDTO executeEnterpriseOperation(CreateCustomerRequest request) {
        log.info("[ENTERPRISE-SERVICE] Processing operation for Customer & Account Management - Ref: {}, Amount: {}",
                request.getReferenceCode(), request.getAmount());

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new CustomerDomainException("CUSTOMER_INVALID_AMOUNT", "Amount cannot be negative");
        }

        Optional<CustomerRecordEntity> existing = repository.findByReferenceCode(request.getReferenceCode());
        if (existing.isPresent()) {
            log.warn("[IDEMPOTENCY-CHECK] Duplicate operation attempt detected for ref: {}", request.getReferenceCode());
            return mapToDTO(existing.get());
        }

        CustomerRecordEntity entity = new CustomerRecordEntity(
                request.getReferenceCode(),
                request.getOwnerId(),
                request.getAmount(),
                "ACTIVE"
        );
        entity.setMetadataJson("{\"category\": \"" + request.getCategory() + "\", \"environment\": \"PRODUCTION\"}");

        CustomerRecordEntity saved = repository.save(entity);
        CustomerDTO dto = mapToDTO(saved);

        // Publish Event to Kafka Cluster
        eventHandler.publishCustomerCreatedEvent(dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<CustomerDTO> findRecordsPaged(UUID ownerId, Pageable pageable) {
        log.debug("[READ-ONLY] Fetching paged records for owner: {}", ownerId);
        return repository.findAll(pageable).map(this::mapToDTO);
    }

    public CustomerDTO archiveRecord(UUID id) {
        CustomerRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new CustomerDomainException("CUSTOMER_NOT_FOUND", "Record ID not found: " + id));
        entity.setStatus("ARCHIVED");
        entity.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(repository.save(entity));
    }

    private CustomerDTO mapToDTO(CustomerRecordEntity entity) {
        return new CustomerDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
