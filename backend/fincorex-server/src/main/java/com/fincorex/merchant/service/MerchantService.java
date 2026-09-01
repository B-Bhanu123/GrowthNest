package com.fincorex.merchant.service;

import com.fincorex.merchant.dto.MerchantDTO;
import com.fincorex.merchant.entity.MerchantRecordEntity;
import com.fincorex.merchant.repository.MerchantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class MerchantService {

    private final MerchantRepository repository;

    @Autowired
    public MerchantService(MerchantRepository repository) {
        this.repository = repository;
    }

    public MerchantDTO createRecord(String referenceCode, UUID ownerId, BigDecimal amount, String status) {
        MerchantRecordEntity entity = new MerchantRecordEntity(referenceCode, ownerId, amount, status);
        MerchantRecordEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public MerchantDTO getByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Merchant record not found for ref: " + referenceCode));
    }

    @Transactional(readOnly = true)
    public List<MerchantDTO> getByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public MerchantDTO updateStatus(UUID id, String newStatus) {
        MerchantRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + id));
        entity.setStatus(newStatus);
        MerchantRecordEntity updated = repository.save(entity);
        return mapToDTO(updated);
    }

    private MerchantDTO mapToDTO(MerchantRecordEntity entity) {
        return new MerchantDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
