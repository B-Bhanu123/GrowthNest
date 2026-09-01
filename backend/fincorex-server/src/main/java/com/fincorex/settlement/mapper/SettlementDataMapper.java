package com.fincorex.settlement.mapper;

import com.fincorex.settlement.dto.SettlementDTO;
import com.fincorex.settlement.entity.SettlementRecordEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MapStruct / ModelMapper equivalent converter for Merchant Batch Settlement
 */
@Component
public class SettlementDataMapper {

    public SettlementDTO toDTO(SettlementRecordEntity entity) {
        if (entity == null) return null;
        SettlementDTO dto = new SettlementDTO();
        dto.setId(entity.getId());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setOwnerId(entity.getOwnerId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public SettlementRecordEntity toEntity(SettlementDTO dto) {
        if (dto == null) return null;
        SettlementRecordEntity entity = new SettlementRecordEntity();
        entity.setId(dto.getId());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setOwnerId(dto.getOwnerId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
