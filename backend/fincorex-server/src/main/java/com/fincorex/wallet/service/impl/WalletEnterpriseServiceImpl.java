package com.fincorex.wallet.service.impl;

import com.fincorex.wallet.dto.WalletDTO;
import com.fincorex.wallet.dto.CreateWalletRequest;
import com.fincorex.wallet.entity.WalletRecordEntity;
import com.fincorex.wallet.exception.WalletDomainException;
import com.fincorex.wallet.repository.WalletRepository;
import com.fincorex.wallet.service.WalletService;
import com.fincorex.wallet.event.WalletEventHandler;
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
 * Enterprise Service Implementation for Stored-Value Digital Wallet
 * Implements strict ACID transactional isolation, idempotency validation, and event publishing.
 */
@Service("walletEnterpriseServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
public class WalletEnterpriseServiceImpl extends WalletService {

    private static final Logger log = LoggerFactory.getLogger(WalletEnterpriseServiceImpl.class);

    private final WalletRepository repository;
    private final WalletEventHandler eventHandler;

    @Autowired
    public WalletEnterpriseServiceImpl(WalletRepository repository, WalletEventHandler eventHandler) {
        super(repository);
        this.repository = repository;
        this.eventHandler = eventHandler;
    }

    public WalletDTO executeEnterpriseOperation(CreateWalletRequest request) {
        log.info("[ENTERPRISE-SERVICE] Processing operation for Stored-Value Digital Wallet - Ref: {}, Amount: {}",
                request.getReferenceCode(), request.getAmount());

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new WalletDomainException("WALLET_INVALID_AMOUNT", "Amount cannot be negative");
        }

        Optional<WalletRecordEntity> existing = repository.findByReferenceCode(request.getReferenceCode());
        if (existing.isPresent()) {
            log.warn("[IDEMPOTENCY-CHECK] Duplicate operation attempt detected for ref: {}", request.getReferenceCode());
            return mapToDTO(existing.get());
        }

        WalletRecordEntity entity = new WalletRecordEntity(
                request.getReferenceCode(),
                request.getOwnerId(),
                request.getAmount(),
                "ACTIVE"
        );
        entity.setMetadataJson("{\"category\": \"" + request.getCategory() + "\", \"environment\": \"PRODUCTION\"}");

        WalletRecordEntity saved = repository.save(entity);
        WalletDTO dto = mapToDTO(saved);

        // Publish Event to Kafka Cluster
        eventHandler.publishWalletCreatedEvent(dto);

        return dto;
    }

    @Transactional(readOnly = true)
    public Page<WalletDTO> findRecordsPaged(UUID ownerId, Pageable pageable) {
        log.debug("[READ-ONLY] Fetching paged records for owner: {}", ownerId);
        return repository.findAll(pageable).map(this::mapToDTO);
    }

    public WalletDTO archiveRecord(UUID id) {
        WalletRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new WalletDomainException("WALLET_NOT_FOUND", "Record ID not found: " + id));
        entity.setStatus("ARCHIVED");
        entity.setUpdatedAt(LocalDateTime.now());
        return mapToDTO(repository.save(entity));
    }

    private WalletDTO mapToDTO(WalletRecordEntity entity) {
        return new WalletDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
