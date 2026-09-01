package com.fincorex.transaction.service.impl;

import com.fincorex.transaction.dto.TransactionDTO;
import com.fincorex.transaction.dto.CreateTransactionRequest;
import com.fincorex.transaction.entity.TransactionRecordEntity;
import com.fincorex.transaction.exception.TransactionDomainException;
import com.fincorex.transaction.repository.TransactionRepository;
import com.fincorex.transaction.service.TransactionService;
import com.fincorex.transaction.event.TransactionEventHandler;
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
 * Enterprise Service Implementation for Transaction Processing Core
 * Implements strict ACID transactional isolation, idempotency validation, and event publishing.
 */
@Service("transactionEnterpriseServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class TransactionEnterpriseServiceImpl extends TransactionService {

    private static final Logger log = LoggerFactory.getLogger(TransactionEnterpriseServiceImpl.class);

    private final TransactionRepository repository;
    private final TransactionEventHandler eventHandler;

    @Autowired
    public TransactionEnterpriseServiceImpl(TransactionRepository repository, TransactionEventHandler eventHandler) {
        super(repository);
        this.repository = repository;
        this.eventHandler = eventHandler;
    }

    public TransactionDTO executeEnterpriseOperation(CreateTransactionRequest request) {
        log.info("[ENTERPRISE-SERVICE] Processing operation for Transaction Processing Core - Ref: {}, Amount: {}",
                request.getReferenceCode(), request.getAmount());

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new TransactionDomainException("TRANSACTION_INVALID_AMOUNT", "Amount cannot be negative");
        }

        Optional<TransactionRecordEntity> existing = repository.findByReferenceCode(request.getReferenceCode());
        if (existing.isPresent()) {
            log.warn("[IDEMPOTENCY-CHECK] Duplicate operation attempt detected for ref: {}", request.getReferenceCode());
            return mapToDTO(existing.get());
        }

        TransactionRecordEntity entity = new TransactionRecordEntity(
                request.getReferenceCode(),
                request.getOwnerId(),
                request.getAmount(),
                "ACTIVE"
        );
        entity.setMetadataJson("{\"category\": \"" + request.getCategory() + "\", \"environment\": \"PRODUCTION\"}");

        TransactionRecordEntity saved = repository.save(entity);
        TransactionDTO dto = mapToDTO(saved);

        // Publish Event to Kafka Cluster
        eventHandler.publishTransactionCreatedEvent(dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<TransactionDTO> findRecordsPaged(UUID ownerId, Pageable pageable) {
        log.debug("[READ-ONLY] Fetching paged records for owner: {}", ownerId);
        return repository.findAll(pageable).map(this::mapToDTO);
    }

    public TransactionDTO archiveRecord(UUID id) {
        TransactionRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new TransactionDomainException("TRANSACTION_NOT_FOUND", "Record ID not found: " + id));
        entity.setStatus("ARCHIVED");
        entity.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(repository.save(entity));
    }

    private TransactionDTO mapToDTO(TransactionRecordEntity entity) {
        return new TransactionDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
