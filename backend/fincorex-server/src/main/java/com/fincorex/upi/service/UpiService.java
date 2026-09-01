package com.fincorex.upi.service;

import com.fincorex.upi.dto.UpiDTO;
import com.fincorex.upi.entity.UpiRecordEntity;
import com.fincorex.upi.repository.UpiRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UpiService {

    private final UpiRepository repository;

    @Autowired
    public UpiService(UpiRepository repository) {
        this.repository = repository;
    }

    public UpiDTO createRecord(String referenceCode, UUID ownerId, BigDecimal amount, String status) {
        UpiRecordEntity entity = new UpiRecordEntity(referenceCode, ownerId, amount, status);
        UpiRecordEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public UpiDTO getByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Upi record not found for ref: " + referenceCode));
    }

    @Transactional(readOnly = true)
    public List<UpiDTO> getByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public UpiDTO updateStatus(UUID id, String newStatus) {
        UpiRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + id));
        entity.setStatus(newStatus);
        UpiRecordEntity updated = repository.save(entity);
        return mapToDTO(updated);
    }

    private UpiDTO mapToDTO(UpiRecordEntity entity) {
        return new UpiDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
