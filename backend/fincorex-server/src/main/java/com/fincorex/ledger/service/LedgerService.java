package com.fincorex.ledger.service;

import com.fincorex.ledger.dto.LedgerDTO;
import com.fincorex.ledger.entity.LedgerRecordEntity;
import com.fincorex.ledger.repository.LedgerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class LedgerService {

    private final LedgerRepository repository;

    @Autowired
    public LedgerService(LedgerRepository repository) {
        this.repository = repository;
    }

    public LedgerDTO createRecord(String referenceCode, UUID ownerId, BigDecimal amount, String status) {
        LedgerRecordEntity entity = new LedgerRecordEntity(referenceCode, ownerId, amount, status);
        LedgerRecordEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public LedgerDTO getByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Ledger record not found for ref: " + referenceCode));
    }

    @Transactional(readOnly = true)
    public List<LedgerDTO> getByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public LedgerDTO updateStatus(UUID id, String newStatus) {
        LedgerRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + id));
        entity.setStatus(newStatus);
        LedgerRecordEntity updated = repository.save(entity);
        return mapToDTO(updated);
    }

    private LedgerDTO mapToDTO(LedgerRecordEntity entity) {
        return new LedgerDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
