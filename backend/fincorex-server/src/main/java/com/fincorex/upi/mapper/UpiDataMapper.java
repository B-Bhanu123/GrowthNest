package com.fincorex.upi.mapper;

import com.fincorex.upi.dto.UpiDTO;
import com.fincorex.upi.entity.UpiRecordEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MapStruct / ModelMapper equivalent converter for UPI Instant Transfer Network
 */
@Component
public class UpiDataMapper {

    public UpiDTO toDTO(UpiRecordEntity entity) {
        if (entity == null) return null;
        UpiDTO dto = new UpiDTO();
        dto.setId(entity.getId());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setOwnerId(entity.getOwnerId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public UpiRecordEntity toEntity(UpiDTO dto) {
        if (dto == null) return null;
        UpiRecordEntity entity = new UpiRecordEntity();
        entity.setId(dto.getId());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setOwnerId(dto.getOwnerId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
