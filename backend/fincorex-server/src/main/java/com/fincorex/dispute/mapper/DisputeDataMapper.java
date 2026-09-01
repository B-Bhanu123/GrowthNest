package com.fincorex.dispute.mapper;

import com.fincorex.dispute.dto.DisputeDTO;
import com.fincorex.dispute.entity.DisputeRecordEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MapStruct / ModelMapper equivalent converter for Dispute & Chargeback Handling
 */
@Component
public class DisputeDataMapper {

    public DisputeDTO toDTO(DisputeRecordEntity entity) {
        if (entity == null) return null;
        DisputeDTO dto = new DisputeDTO();
        dto.setId(entity.getId());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setOwnerId(entity.getOwnerId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public DisputeRecordEntity toEntity(DisputeDTO dto) {
        if (dto == null) return null;
        DisputeRecordEntity entity = new DisputeRecordEntity();
        entity.setId(dto.getId());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setOwnerId(dto.getOwnerId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
