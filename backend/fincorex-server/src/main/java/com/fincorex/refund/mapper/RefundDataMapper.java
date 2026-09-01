package com.fincorex.refund.mapper;

import com.fincorex.refund.dto.RefundDTO;
import com.fincorex.refund.entity.RefundRecordEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MapStruct / ModelMapper equivalent converter for Refund Management
 */
@Component
public class RefundDataMapper {

    public RefundDTO toDTO(RefundRecordEntity entity) {
        if (entity == null) return null;
        RefundDTO dto = new RefundDTO();
        dto.setId(entity.getId());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setOwnerId(entity.getOwnerId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public RefundRecordEntity toEntity(RefundDTO dto) {
        if (dto == null) return null;
        RefundRecordEntity entity = new RefundRecordEntity();
        entity.setId(dto.getId());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setOwnerId(dto.getOwnerId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
