package com.fincorex.reconciliation.service;

import com.fincorex.reconciliation.dto.ReconciliationDTO;
import com.fincorex.reconciliation.entity.ReconciliationRecordEntity;
import com.fincorex.reconciliation.repository.ReconciliationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReconciliationService {

    private final ReconciliationRepository repository;

    @Autowired
    public ReconciliationService(ReconciliationRepository repository) {
        this.repository = repository;
    }

    public ReconciliationDTO createRecord(String referenceCode, UUID ownerId, BigDecimal amount, String status) {
        ReconciliationRecordEntity entity = new ReconciliationRecordEntity(referenceCode, ownerId, amount, status);
        ReconciliationRecordEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public ReconciliationDTO getByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Reconciliation record not found for ref: " + referenceCode));
    }

    @Transactional(readOnly = true)
    public List<ReconciliationDTO> getByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ReconciliationDTO updateStatus(UUID id, String newStatus) {
        ReconciliationRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + id));
        entity.setStatus(newStatus);
        ReconciliationRecordEntity updated = repository.save(entity);
        return mapToDTO(updated);
    }

    private ReconciliationDTO mapToDTO(ReconciliationRecordEntity entity) {
        return new ReconciliationDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
