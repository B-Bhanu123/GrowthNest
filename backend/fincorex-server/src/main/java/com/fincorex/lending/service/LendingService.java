package com.fincorex.lending.service;

import com.fincorex.lending.dto.LendingDTO;
import com.fincorex.lending.entity.LendingRecordEntity;
import com.fincorex.lending.repository.LendingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class LendingService {

    private final LendingRepository repository;

    @Autowired
    public LendingService(LendingRepository repository) {
        this.repository = repository;
    }

    public LendingDTO createRecord(String referenceCode, UUID ownerId, BigDecimal amount, String status) {
        LendingRecordEntity entity = new LendingRecordEntity(referenceCode, ownerId, amount, status);
        LendingRecordEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public LendingDTO getByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Lending record not found for ref: " + referenceCode));
    }

    @Transactional(readOnly = true)
    public List<LendingDTO> getByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public LendingDTO updateStatus(UUID id, String newStatus) {
        LendingRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + id));
        entity.setStatus(newStatus);
        LendingRecordEntity updated = repository.save(entity);
        return mapToDTO(updated);
    }

    private LendingDTO mapToDTO(LendingRecordEntity entity) {
        return new LendingDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
