package com.fincorex.identity.service;

import com.fincorex.identity.dto.IdentityDTO;
import com.fincorex.identity.entity.IdentityRecordEntity;
import com.fincorex.identity.repository.IdentityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class IdentityService {

    private final IdentityRepository repository;

    @Autowired
    public IdentityService(IdentityRepository repository) {
        this.repository = repository;
    }

    public IdentityDTO createRecord(String referenceCode, UUID ownerId, BigDecimal amount, String status) {
        IdentityRecordEntity entity = new IdentityRecordEntity(referenceCode, ownerId, amount, status);
        IdentityRecordEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public IdentityDTO getByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Identity record not found for ref: " + referenceCode));
    }

    @Transactional(readOnly = true)
    public List<IdentityDTO> getByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public IdentityDTO updateStatus(UUID id, String newStatus) {
        IdentityRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + id));
        entity.setStatus(newStatus);
        IdentityRecordEntity updated = repository.save(entity);
        return mapToDTO(updated);
    }

    private IdentityDTO mapToDTO(IdentityRecordEntity entity) {
        return new IdentityDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
