package com.fincorex.admin.service;

import com.fincorex.admin.dto.AdminDTO;
import com.fincorex.admin.entity.AdminRecordEntity;
import com.fincorex.admin.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminService {

    private final AdminRepository repository;

    @Autowired
    public AdminService(AdminRepository repository) {
        this.repository = repository;
    }

    public AdminDTO createRecord(String referenceCode, UUID ownerId, BigDecimal amount, String status) {
        AdminRecordEntity entity = new AdminRecordEntity(referenceCode, ownerId, amount, status);
        AdminRecordEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public AdminDTO getByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Admin record not found for ref: " + referenceCode));
    }

    @Transactional(readOnly = true)
    public List<AdminDTO> getByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public AdminDTO updateStatus(UUID id, String newStatus) {
        AdminRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + id));
        entity.setStatus(newStatus);
        AdminRecordEntity updated = repository.save(entity);
        return mapToDTO(updated);
    }

    private AdminDTO mapToDTO(AdminRecordEntity entity) {
        return new AdminDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
