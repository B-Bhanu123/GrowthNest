package com.fincorex.accounting.service.impl;

import com.fincorex.accounting.dto.AccountingDTO;
import com.fincorex.accounting.dto.CreateAccountingRequest;
import com.fincorex.accounting.entity.AccountingRecordEntity;
import com.fincorex.accounting.exception.AccountingDomainException;
import com.fincorex.accounting.repository.AccountingRepository;
import com.fincorex.accounting.service.AccountingService;
import com.fincorex.accounting.event.AccountingEventHandler;
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
 * Enterprise Service Implementation for General Accounting & Trial Balance
 * Implements strict ACID transactional isolation, idempotency validation, and event publishing.
 */
@Service("accountingEnterpriseServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class AccountingEnterpriseServiceImpl extends AccountingService {

    private static final Logger log = LoggerFactory.getLogger(AccountingEnterpriseServiceImpl.class);

    private final AccountingRepository repository;
    private final AccountingEventHandler eventHandler;

    @Autowired
    public AccountingEnterpriseServiceImpl(AccountingRepository repository, AccountingEventHandler eventHandler) {
        super(repository);
        this.repository = repository;
        this.eventHandler = eventHandler;
    }

    public AccountingDTO executeEnterpriseOperation(CreateAccountingRequest request) {
        log.info("[ENTERPRISE-SERVICE] Processing operation for General Accounting & Trial Balance - Ref: {}, Amount: {}",
                request.getReferenceCode(), request.getAmount());

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new AccountingDomainException("ACCOUNTING_INVALID_AMOUNT", "Amount cannot be negative");
        }

        Optional<AccountingRecordEntity> existing = repository.findByReferenceCode(request.getReferenceCode());
        if (existing.isPresent()) {
            log.warn("[IDEMPOTENCY-CHECK] Duplicate operation attempt detected for ref: {}", request.getReferenceCode());
            return mapToDTO(existing.get());
        }

        AccountingRecordEntity entity = new AccountingRecordEntity(
                request.getReferenceCode(),
                request.getOwnerId(),
                request.getAmount(),
                "ACTIVE"
        );
        entity.setMetadataJson("{\"category\": \"" + request.getCategory() + "\", \"environment\": \"PRODUCTION\"}");

        AccountingRecordEntity saved = repository.save(entity);
        AccountingDTO dto = mapToDTO(saved);

        // Publish Event to Kafka Cluster
        eventHandler.publishAccountingCreatedEvent(dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<AccountingDTO> findRecordsPaged(UUID ownerId, Pageable pageable) {
        log.debug("[READ-ONLY] Fetching paged records for owner: {}", ownerId);
        return repository.findAll(pageable).map(this::mapToDTO);
    }

    public AccountingDTO archiveRecord(UUID id) {
        AccountingRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new AccountingDomainException("ACCOUNTING_NOT_FOUND", "Record ID not found: " + id));
        entity.setStatus("ARCHIVED");
        entity.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(repository.save(entity));
    }

    private AccountingDTO mapToDTO(AccountingRecordEntity entity) {
        return new AccountingDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
