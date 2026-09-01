package com.fincorex.admin.mapper;

import com.fincorex.admin.dto.AdminDTO;
import com.fincorex.admin.entity.AdminRecordEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MapStruct / ModelMapper equivalent converter for Admin & Operations Center
 */
@Component
public class AdminDataMapper {

    public AdminDTO toDTO(AdminRecordEntity entity) {
        if (entity == null) return null;
        AdminDTO dto = new AdminDTO();
        dto.setId(entity.getId());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setOwnerId(entity.getOwnerId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public AdminRecordEntity toEntity(AdminDTO dto) {
        if (dto == null) return null;
        AdminRecordEntity entity = new AdminRecordEntity();
        entity.setId(dto.getId());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setOwnerId(dto.getOwnerId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
