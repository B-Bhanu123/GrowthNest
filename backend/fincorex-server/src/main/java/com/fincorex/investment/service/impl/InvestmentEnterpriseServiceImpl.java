package com.fincorex.investment.service.impl;

import com.fincorex.investment.dto.InvestmentDTO;
import com.fincorex.investment.dto.CreateInvestmentRequest;
import com.fincorex.investment.entity.InvestmentRecordEntity;
import com.fincorex.investment.exception.InvestmentDomainException;
import com.fincorex.investment.repository.InvestmentRepository;
import com.fincorex.investment.service.InvestmentService;
import com.fincorex.investment.event.InvestmentEventHandler;
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
 * Enterprise Service Implementation for Investment & Portfolio Platform
 * Implements strict ACID transactional isolation, idempotency validation, and event publishing.
 */
@Service("investmentEnterpriseServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class InvestmentEnterpriseServiceImpl extends InvestmentService {

    private static final Logger log = LoggerFactory.getLogger(InvestmentEnterpriseServiceImpl.class);

    private final InvestmentRepository repository;
    private final InvestmentEventHandler eventHandler;

    @Autowired
    public InvestmentEnterpriseServiceImpl(InvestmentRepository repository, InvestmentEventHandler eventHandler) {
        super(repository);
        this.repository = repository;
        this.eventHandler = eventHandler;
    }

    public InvestmentDTO executeEnterpriseOperation(CreateInvestmentRequest request) {
        log.info("[ENTERPRISE-SERVICE] Processing operation for Investment & Portfolio Platform - Ref: {}, Amount: {}",
                request.getReferenceCode(), request.getAmount());

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvestmentDomainException("INVESTMENT_INVALID_AMOUNT", "Amount cannot be negative");
        }

        Optional<InvestmentRecordEntity> existing = repository.findByReferenceCode(request.getReferenceCode());
        if (existing.isPresent()) {
            log.warn("[IDEMPOTENCY-CHECK] Duplicate operation attempt detected for ref: {}", request.getReferenceCode());
            return mapToDTO(existing.get());
        }

        InvestmentRecordEntity entity = new InvestmentRecordEntity(
                request.getReferenceCode(),
                request.getOwnerId(),
                request.getAmount(),
                "ACTIVE"
        );
        entity.setMetadataJson("{\"category\": \"" + request.getCategory() + "\", \"environment\": \"PRODUCTION\"}");

        InvestmentRecordEntity saved = repository.save(entity);
        InvestmentDTO dto = mapToDTO(saved);

        // Publish Event to Kafka Cluster
        eventHandler.publishInvestmentCreatedEvent(dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<InvestmentDTO> findRecordsPaged(UUID ownerId, Pageable pageable) {
        log.debug("[READ-ONLY] Fetching paged records for owner: {}", ownerId);
        return repository.findAll(pageable).map(this::mapToDTO);
    }

    public InvestmentDTO archiveRecord(UUID id) {
        InvestmentRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new InvestmentDomainException("INVESTMENT_NOT_FOUND", "Record ID not found: " + id));
        entity.setStatus("ARCHIVED");
        entity.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(repository.save(entity));
    }

    private InvestmentDTO mapToDTO(InvestmentRecordEntity entity) {
        return new InvestmentDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
