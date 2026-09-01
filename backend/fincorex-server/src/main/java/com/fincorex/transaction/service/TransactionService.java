package com.fincorex.transaction.service;

import com.fincorex.transaction.dto.TransactionDTO;
import com.fincorex.transaction.entity.TransactionRecordEntity;
import com.fincorex.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class TransactionService {

    private final TransactionRepository repository;

    @Autowired
    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    public TransactionDTO createRecord(String referenceCode, UUID ownerId, BigDecimal amount, String status) {
        TransactionRecordEntity entity = new TransactionRecordEntity(referenceCode, ownerId, amount, status);
        TransactionRecordEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public TransactionDTO getByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Transaction record not found for ref: " + referenceCode));
    }

    @Transactional(readOnly = true)
    public List<TransactionDTO> getByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public TransactionDTO updateStatus(UUID id, String newStatus) {
        TransactionRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + id));
        entity.setStatus(newStatus);
        TransactionRecordEntity updated = repository.save(entity);
        return mapToDTO(updated);
    }

    private TransactionDTO mapToDTO(TransactionRecordEntity entity) {
        return new TransactionDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
