package com.fincorex.settlement.service;

import com.fincorex.settlement.dto.SettlementDTO;
import com.fincorex.settlement.entity.SettlementRecordEntity;
import com.fincorex.settlement.repository.SettlementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class SettlementService {

    private final SettlementRepository repository;

    @Autowired
    public SettlementService(SettlementRepository repository) {
        this.repository = repository;
    }

    public SettlementDTO createRecord(String referenceCode, UUID ownerId, BigDecimal amount, String status) {
        SettlementRecordEntity entity = new SettlementRecordEntity(referenceCode, ownerId, amount, status);
        SettlementRecordEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public SettlementDTO getByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Settlement record not found for ref: " + referenceCode));
    }

    @Transactional(readOnly = true)
    public List<SettlementDTO> getByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public SettlementDTO updateStatus(UUID id, String newStatus) {
        SettlementRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + id));
        entity.setStatus(newStatus);
        SettlementRecordEntity updated = repository.save(entity);
        return mapToDTO(updated);
    }

    private SettlementDTO mapToDTO(SettlementRecordEntity entity) {
        return new SettlementDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
