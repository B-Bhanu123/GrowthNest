package com.fincorex.investment.service;

import com.fincorex.investment.dto.InvestmentDTO;
import com.fincorex.investment.entity.InvestmentRecordEntity;
import com.fincorex.investment.repository.InvestmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class InvestmentService {

    private final InvestmentRepository repository;

    @Autowired
    public InvestmentService(InvestmentRepository repository) {
        this.repository = repository;
    }

    public InvestmentDTO createRecord(String referenceCode, UUID ownerId, BigDecimal amount, String status) {
        InvestmentRecordEntity entity = new InvestmentRecordEntity(referenceCode, ownerId, amount, status);
        InvestmentRecordEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public InvestmentDTO getByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Investment record not found for ref: " + referenceCode));
    }

    @Transactional(readOnly = true)
    public List<InvestmentDTO> getByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public InvestmentDTO updateStatus(UUID id, String newStatus) {
        InvestmentRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + id));
        entity.setStatus(newStatus);
        InvestmentRecordEntity updated = repository.save(entity);
        return mapToDTO(updated);
    }

    private InvestmentDTO mapToDTO(InvestmentRecordEntity entity) {
        return new InvestmentDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
