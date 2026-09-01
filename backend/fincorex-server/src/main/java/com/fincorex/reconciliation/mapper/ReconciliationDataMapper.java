package com.fincorex.reconciliation.mapper;

import com.fincorex.reconciliation.dto.ReconciliationDTO;
import com.fincorex.reconciliation.entity.ReconciliationRecordEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MapStruct / ModelMapper equivalent converter for Automated Bank Reconciliation
 */
@Component
public class ReconciliationDataMapper {

    public ReconciliationDTO toDTO(ReconciliationRecordEntity entity) {
        if (entity == null) return null;
        ReconciliationDTO dto = new ReconciliationDTO();
        dto.setId(entity.getId());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setOwnerId(entity.getOwnerId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public ReconciliationRecordEntity toEntity(ReconciliationDTO dto) {
        if (dto == null) return null;
        ReconciliationRecordEntity entity = new ReconciliationRecordEntity();
        entity.setId(dto.getId());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setOwnerId(dto.getOwnerId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
