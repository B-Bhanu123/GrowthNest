package com.fincorex.wallet.service;

import com.fincorex.wallet.dto.WalletDTO;
import com.fincorex.wallet.entity.WalletRecordEntity;
import com.fincorex.wallet.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class WalletService {

    private final WalletRepository repository;

    @Autowired
    public WalletService(WalletRepository repository) {
        this.repository = repository;
    }

    public WalletDTO createRecord(String referenceCode, UUID ownerId, BigDecimal amount, String status) {
        WalletRecordEntity entity = new WalletRecordEntity(referenceCode, ownerId, amount, status);
        WalletRecordEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public WalletDTO getByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Wallet record not found for ref: " + referenceCode));
    }

    @Transactional(readOnly = true)
    public List<WalletDTO> getByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public WalletDTO updateStatus(UUID id, String newStatus) {
        WalletRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + id));
        entity.setStatus(newStatus);
        WalletRecordEntity updated = repository.save(entity);
        return mapToDTO(updated);
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
