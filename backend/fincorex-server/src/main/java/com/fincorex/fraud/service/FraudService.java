package com.fincorex.fraud.service;

import com.fincorex.fraud.dto.FraudDTO;
import com.fincorex.fraud.entity.FraudRecordEntity;
import com.fincorex.fraud.repository.FraudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class FraudService {

    private final FraudRepository repository;

    @Autowired
    public FraudService(FraudRepository repository) {
        this.repository = repository;
    }

    public FraudDTO createRecord(String referenceCode, UUID ownerId, BigDecimal amount, String status) {
        FraudRecordEntity entity = new FraudRecordEntity(referenceCode, ownerId, amount, status);
        FraudRecordEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public FraudDTO getByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Fraud record not found for ref: " + referenceCode));
    }

    @Transactional(readOnly = true)
    public List<FraudDTO> getByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public FraudDTO updateStatus(UUID id, String newStatus) {
        FraudRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + id));
        entity.setStatus(newStatus);
        FraudRecordEntity updated = repository.save(entity);
        return mapToDTO(updated);
    }

    private FraudDTO mapToDTO(FraudRecordEntity entity) {
        return new FraudDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
