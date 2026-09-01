package com.fincorex.payment.service.impl;

import com.fincorex.payment.dto.PaymentDTO;
import com.fincorex.payment.dto.CreatePaymentRequest;
import com.fincorex.payment.entity.PaymentRecordEntity;
import com.fincorex.payment.exception.PaymentDomainException;
import com.fincorex.payment.repository.PaymentRepository;
import com.fincorex.payment.service.PaymentService;
import com.fincorex.payment.event.PaymentEventHandler;
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
 * Enterprise Service Implementation for Payment Gateway Orchestration
 * Implements strict ACID transactional isolation, idempotency validation, and event publishing.
 */
@Service("paymentEnterpriseServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class PaymentEnterpriseServiceImpl extends PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentEnterpriseServiceImpl.class);

    private final PaymentRepository repository;
    private final PaymentEventHandler eventHandler;

    @Autowired
    public PaymentEnterpriseServiceImpl(PaymentRepository repository, PaymentEventHandler eventHandler) {
        super(repository);
        this.repository = repository;
        this.eventHandler = eventHandler;
    }

    public PaymentDTO executeEnterpriseOperation(CreatePaymentRequest request) {
        log.info("[ENTERPRISE-SERVICE] Processing operation for Payment Gateway Orchestration - Ref: {}, Amount: {}",
                request.getReferenceCode(), request.getAmount());

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new PaymentDomainException("PAYMENT_INVALID_AMOUNT", "Amount cannot be negative");
        }

        Optional<PaymentRecordEntity> existing = repository.findByReferenceCode(request.getReferenceCode());
        if (existing.isPresent()) {
            log.warn("[IDEMPOTENCY-CHECK] Duplicate operation attempt detected for ref: {}", request.getReferenceCode());
            return mapToDTO(existing.get());
        }

        PaymentRecordEntity entity = new PaymentRecordEntity(
                request.getReferenceCode(),
                request.getOwnerId(),
                request.getAmount(),
                "ACTIVE"
        );
        entity.setMetadataJson("{\"category\": \"" + request.getCategory() + "\", \"environment\": \"PRODUCTION\"}");

        PaymentRecordEntity saved = repository.save(entity);
        PaymentDTO dto = mapToDTO(saved);

        // Publish Event to Kafka Cluster
        eventHandler.publishPaymentCreatedEvent(dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<PaymentDTO> findRecordsPaged(UUID ownerId, Pageable pageable) {
        log.debug("[READ-ONLY] Fetching paged records for owner: {}", ownerId);
        return repository.findAll(pageable).map(this::mapToDTO);
    }

    public PaymentDTO archiveRecord(UUID id) {
        PaymentRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new PaymentDomainException("PAYMENT_NOT_FOUND", "Record ID not found: " + id));
        entity.setStatus("ARCHIVED");
        entity.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(repository.save(entity));
    }

    private PaymentDTO mapToDTO(PaymentRecordEntity entity) {
        return new PaymentDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
