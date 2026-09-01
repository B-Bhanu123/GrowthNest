package com.fincorex.accounting.service;

import com.fincorex.accounting.dto.AccountingDTO;
import com.fincorex.accounting.entity.AccountingRecordEntity;
import com.fincorex.accounting.repository.AccountingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AccountingService {

    private final AccountingRepository repository;

    @Autowired
    public AccountingService(AccountingRepository repository) {
        this.repository = repository;
    }

    public AccountingDTO createRecord(String referenceCode, UUID ownerId, BigDecimal amount, String status) {
        AccountingRecordEntity entity = new AccountingRecordEntity(referenceCode, ownerId, amount, status);
        AccountingRecordEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public AccountingDTO getByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Accounting record not found for ref: " + referenceCode));
    }

    @Transactional(readOnly = true)
    public List<AccountingDTO> getByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public AccountingDTO updateStatus(UUID id, String newStatus) {
        AccountingRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + id));
        entity.setStatus(newStatus);
        AccountingRecordEntity updated = repository.save(entity);
        return mapToDTO(updated);
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
