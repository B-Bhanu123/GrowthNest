package com.fincorex.dispute.service;

import com.fincorex.dispute.dto.DisputeDTO;
import com.fincorex.dispute.entity.DisputeRecordEntity;
import com.fincorex.dispute.repository.DisputeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class DisputeService {

    private final DisputeRepository repository;

    @Autowired
    public DisputeService(DisputeRepository repository) {
        this.repository = repository;
    }

    public DisputeDTO createRecord(String referenceCode, UUID ownerId, BigDecimal amount, String status) {
        DisputeRecordEntity entity = new DisputeRecordEntity(referenceCode, ownerId, amount, status);
        DisputeRecordEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public DisputeDTO getByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Dispute record not found for ref: " + referenceCode));
    }

    @Transactional(readOnly = true)
    public List<DisputeDTO> getByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public DisputeDTO updateStatus(UUID id, String newStatus) {
        DisputeRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + id));
        entity.setStatus(newStatus);
        DisputeRecordEntity updated = repository.save(entity);
        return mapToDTO(updated);
    }

    private DisputeDTO mapToDTO(DisputeRecordEntity entity) {
        return new DisputeDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
