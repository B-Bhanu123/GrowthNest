package com.fincorex.audit.service;

import com.fincorex.audit.dto.AuditDTO;
import com.fincorex.audit.entity.AuditRecordEntity;
import com.fincorex.audit.repository.AuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuditService {

    private final AuditRepository repository;

    @Autowired
    public AuditService(AuditRepository repository) {
        this.repository = repository;
    }

    public AuditDTO createRecord(String referenceCode, UUID ownerId, BigDecimal amount, String status) {
        AuditRecordEntity entity = new AuditRecordEntity(referenceCode, ownerId, amount, status);
        AuditRecordEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public AuditDTO getByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Audit record not found for ref: " + referenceCode));
    }

    @Transactional(readOnly = true)
    public List<AuditDTO> getByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public AuditDTO updateStatus(UUID id, String newStatus) {
        AuditRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + id));
        entity.setStatus(newStatus);
        AuditRecordEntity updated = repository.save(entity);
        return mapToDTO(updated);
    }

    private AuditDTO mapToDTO(AuditRecordEntity entity) {
        return new AuditDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
