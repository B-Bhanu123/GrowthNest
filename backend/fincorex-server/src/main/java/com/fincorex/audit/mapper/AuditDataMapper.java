package com.fincorex.audit.mapper;

import com.fincorex.audit.dto.AuditDTO;
import com.fincorex.audit.entity.AuditRecordEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MapStruct / ModelMapper equivalent converter for Immutable Audit Logging
 */
@Component
public class AuditDataMapper {

    public AuditDTO toDTO(AuditRecordEntity entity) {
        if (entity == null) return null;
        AuditDTO dto = new AuditDTO();
        dto.setId(entity.getId());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setOwnerId(entity.getOwnerId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public AuditRecordEntity toEntity(AuditDTO dto) {
        if (dto == null) return null;
        AuditRecordEntity entity = new AuditRecordEntity();
        entity.setId(dto.getId());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setOwnerId(dto.getOwnerId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
