package com.fincorex.lending.mapper;

import com.fincorex.lending.dto.LendingDTO;
import com.fincorex.lending.entity.LendingRecordEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MapStruct / ModelMapper equivalent converter for Lending & Underwriting Engine
 */
@Component
public class LendingDataMapper {

    public LendingDTO toDTO(LendingRecordEntity entity) {
        if (entity == null) return null;
        LendingDTO dto = new LendingDTO();
        dto.setId(entity.getId());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setOwnerId(entity.getOwnerId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public LendingRecordEntity toEntity(LendingDTO dto) {
        if (dto == null) return null;
        LendingRecordEntity entity = new LendingRecordEntity();
        entity.setId(dto.getId());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setOwnerId(dto.getOwnerId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
