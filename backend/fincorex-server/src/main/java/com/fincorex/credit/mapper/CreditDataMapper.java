package com.fincorex.credit.mapper;

import com.fincorex.credit.dto.CreditDTO;
import com.fincorex.credit.entity.CreditRecordEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MapStruct / ModelMapper equivalent converter for Credit Scoring System
 */
@Component
public class CreditDataMapper {

    public CreditDTO toDTO(CreditRecordEntity entity) {
        if (entity == null) return null;
        CreditDTO dto = new CreditDTO();
        dto.setId(entity.getId());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setOwnerId(entity.getOwnerId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public CreditRecordEntity toEntity(CreditDTO dto) {
        if (dto == null) return null;
        CreditRecordEntity entity = new CreditRecordEntity();
        entity.setId(dto.getId());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setOwnerId(dto.getOwnerId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
