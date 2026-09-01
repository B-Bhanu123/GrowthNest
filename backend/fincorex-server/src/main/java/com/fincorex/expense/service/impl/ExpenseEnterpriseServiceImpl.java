package com.fincorex.expense.service.impl;

import com.fincorex.expense.dto.ExpenseDTO;
import com.fincorex.expense.dto.CreateExpenseRequest;
import com.fincorex.expense.entity.ExpenseRecordEntity;
import com.fincorex.expense.exception.ExpenseDomainException;
import com.fincorex.expense.repository.ExpenseRepository;
import com.fincorex.expense.service.ExpenseService;
import com.fincorex.expense.event.ExpenseEventHandler;
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
 * Enterprise Service Implementation for Corporate Expense Management
 * Implements strict ACID transactional isolation, idempotency validation, and event publishing.
 */
@Service("expenseEnterpriseServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class ExpenseEnterpriseServiceImpl extends ExpenseService {

    private static final Logger log = LoggerFactory.getLogger(ExpenseEnterpriseServiceImpl.class);

    private final ExpenseRepository repository;
    private final ExpenseEventHandler eventHandler;

    @Autowired
    public ExpenseEnterpriseServiceImpl(ExpenseRepository repository, ExpenseEventHandler eventHandler) {
        super(repository);
        this.repository = repository;
        this.eventHandler = eventHandler;
    }

    public ExpenseDTO executeEnterpriseOperation(CreateExpenseRequest request) {
        log.info("[ENTERPRISE-SERVICE] Processing operation for Corporate Expense Management - Ref: {}, Amount: {}",
                request.getReferenceCode(), request.getAmount());

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new ExpenseDomainException("EXPENSE_INVALID_AMOUNT", "Amount cannot be negative");
        }

        Optional<ExpenseRecordEntity> existing = repository.findByReferenceCode(request.getReferenceCode());
        if (existing.isPresent()) {
            log.warn("[IDEMPOTENCY-CHECK] Duplicate operation attempt detected for ref: {}", request.getReferenceCode());
            return mapToDTO(existing.get());
        }

        ExpenseRecordEntity entity = new ExpenseRecordEntity(
                request.getReferenceCode(),
                request.getOwnerId(),
                request.getAmount(),
                "ACTIVE"
        );
        entity.setMetadataJson("{\"category\": \"" + request.getCategory() + "\", \"environment\": \"PRODUCTION\"}");

        ExpenseRecordEntity saved = repository.save(entity);
        ExpenseDTO dto = mapToDTO(saved);

        // Publish Event to Kafka Cluster
        eventHandler.publishExpenseCreatedEvent(dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<ExpenseDTO> findRecordsPaged(UUID ownerId, Pageable pageable) {
        log.debug("[READ-ONLY] Fetching paged records for owner: {}", ownerId);
        return repository.findAll(pageable).map(this::mapToDTO);
    }

    public ExpenseDTO archiveRecord(UUID id) {
        ExpenseRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new ExpenseDomainException("EXPENSE_NOT_FOUND", "Record ID not found: " + id));
        entity.setStatus("ARCHIVED");
        entity.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(repository.save(entity));
    }

    private ExpenseDTO mapToDTO(ExpenseRecordEntity entity) {
        return new ExpenseDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
