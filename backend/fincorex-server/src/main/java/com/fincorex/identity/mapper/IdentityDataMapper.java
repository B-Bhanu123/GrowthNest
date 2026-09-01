package com.fincorex.identity.mapper;

import com.fincorex.identity.dto.IdentityDTO;
import com.fincorex.identity.entity.IdentityRecordEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MapStruct / ModelMapper equivalent converter for Identity & Access Management
 */
@Component
public class IdentityDataMapper {

    public IdentityDTO toDTO(IdentityRecordEntity entity) {
        if (entity == null) return null;
        IdentityDTO dto = new IdentityDTO();
        dto.setId(entity.getId());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setOwnerId(entity.getOwnerId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public IdentityRecordEntity toEntity(IdentityDTO dto) {
        if (dto == null) return null;
        IdentityRecordEntity entity = new IdentityRecordEntity();
        entity.setId(dto.getId());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setOwnerId(dto.getOwnerId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
