package com.fincorex.refund.service;

import com.fincorex.refund.dto.RefundDTO;
import com.fincorex.refund.entity.RefundRecordEntity;
import com.fincorex.refund.repository.RefundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class RefundService {

    private final RefundRepository repository;

    @Autowired
    public RefundService(RefundRepository repository) {
        this.repository = repository;
    }

    public RefundDTO createRecord(String referenceCode, UUID ownerId, BigDecimal amount, String status) {
        RefundRecordEntity entity = new RefundRecordEntity(referenceCode, ownerId, amount, status);
        RefundRecordEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public RefundDTO getByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Refund record not found for ref: " + referenceCode));
    }

    @Transactional(readOnly = true)
    public List<RefundDTO> getByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public RefundDTO updateStatus(UUID id, String newStatus) {
        RefundRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + id));
        entity.setStatus(newStatus);
        RefundRecordEntity updated = repository.save(entity);
        return mapToDTO(updated);
    }

    private RefundDTO mapToDTO(RefundRecordEntity entity) {
        return new RefundDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
