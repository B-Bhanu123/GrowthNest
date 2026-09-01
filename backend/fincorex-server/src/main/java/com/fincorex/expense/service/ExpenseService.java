package com.fincorex.expense.service;

import com.fincorex.expense.dto.ExpenseDTO;
import com.fincorex.expense.entity.ExpenseRecordEntity;
import com.fincorex.expense.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExpenseService {

    private final ExpenseRepository repository;

    @Autowired
    public ExpenseService(ExpenseRepository repository) {
        this.repository = repository;
    }

    public ExpenseDTO createRecord(String referenceCode, UUID ownerId, BigDecimal amount, String status) {
        ExpenseRecordEntity entity = new ExpenseRecordEntity(referenceCode, ownerId, amount, status);
        ExpenseRecordEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public ExpenseDTO getByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Expense record not found for ref: " + referenceCode));
    }

    @Transactional(readOnly = true)
    public List<ExpenseDTO> getByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ExpenseDTO updateStatus(UUID id, String newStatus) {
        ExpenseRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + id));
        entity.setStatus(newStatus);
        ExpenseRecordEntity updated = repository.save(entity);
        return mapToDTO(updated);
    }

    private ExpenseDTO mapToDTO(ExpenseRecordEntity entity) {
        return new ExpenseDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
