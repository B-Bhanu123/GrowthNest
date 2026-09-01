package com.fincorex.insurance.service;

import com.fincorex.insurance.dto.InsuranceDTO;
import com.fincorex.insurance.entity.InsuranceRecordEntity;
import com.fincorex.insurance.repository.InsuranceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class InsuranceService {

    private final InsuranceRepository repository;

    @Autowired
    public InsuranceService(InsuranceRepository repository) {
        this.repository = repository;
    }

    public InsuranceDTO createRecord(String referenceCode, UUID ownerId, BigDecimal amount, String status) {
        InsuranceRecordEntity entity = new InsuranceRecordEntity(referenceCode, ownerId, amount, status);
        InsuranceRecordEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public InsuranceDTO getByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Insurance record not found for ref: " + referenceCode));
    }

    @Transactional(readOnly = true)
    public List<InsuranceDTO> getByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public InsuranceDTO updateStatus(UUID id, String newStatus) {
        InsuranceRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + id));
        entity.setStatus(newStatus);
        InsuranceRecordEntity updated = repository.save(entity);
        return mapToDTO(updated);
    }

    private InsuranceDTO mapToDTO(InsuranceRecordEntity entity) {
        return new InsuranceDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
