package com.fincorex.payment.service;

import com.fincorex.payment.dto.PaymentDTO;
import com.fincorex.payment.entity.PaymentRecordEntity;
import com.fincorex.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository repository;

    @Autowired
    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }

    public PaymentDTO createRecord(String referenceCode, UUID ownerId, BigDecimal amount, String status) {
        PaymentRecordEntity entity = new PaymentRecordEntity(referenceCode, ownerId, amount, status);
        PaymentRecordEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public PaymentDTO getByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Payment record not found for ref: " + referenceCode));
    }

    @Transactional(readOnly = true)
    public List<PaymentDTO> getByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public PaymentDTO updateStatus(UUID id, String newStatus) {
        PaymentRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + id));
        entity.setStatus(newStatus);
        PaymentRecordEntity updated = repository.save(entity);
        return mapToDTO(updated);
    }

    private PaymentDTO mapToDTO(PaymentRecordEntity entity) {
        return new PaymentDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
