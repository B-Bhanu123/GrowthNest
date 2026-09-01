package com.fincorex.credit.service;

import com.fincorex.credit.dto.CreditDTO;
import com.fincorex.credit.entity.CreditRecordEntity;
import com.fincorex.credit.repository.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CreditService {

    private final CreditRepository repository;

    @Autowired
    public CreditService(CreditRepository repository) {
        this.repository = repository;
    }

    public CreditDTO createRecord(String referenceCode, UUID ownerId, BigDecimal amount, String status) {
        CreditRecordEntity entity = new CreditRecordEntity(referenceCode, ownerId, amount, status);
        CreditRecordEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public CreditDTO getByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Credit record not found for ref: " + referenceCode));
    }

    @Transactional(readOnly = true)
    public List<CreditDTO> getByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public CreditDTO updateStatus(UUID id, String newStatus) {
        CreditRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + id));
        entity.setStatus(newStatus);
        CreditRecordEntity updated = repository.save(entity);
        return mapToDTO(updated);
    }

    private CreditDTO mapToDTO(CreditRecordEntity entity) {
        return new CreditDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
