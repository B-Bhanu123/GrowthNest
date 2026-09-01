package com.fincorex.fraud.mapper;

import com.fincorex.fraud.dto.FraudDTO;
import com.fincorex.fraud.entity.FraudRecordEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MapStruct / ModelMapper equivalent converter for Real-Time Fraud Detection Engine
 */
@Component
public class FraudDataMapper {

    public FraudDTO toDTO(FraudRecordEntity entity) {
        if (entity == null) return null;
        FraudDTO dto = new FraudDTO();
        dto.setId(entity.getId());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setOwnerId(entity.getOwnerId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public FraudRecordEntity toEntity(FraudDTO dto) {
        if (dto == null) return null;
        FraudRecordEntity entity = new FraudRecordEntity();
        entity.setId(dto.getId());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setOwnerId(dto.getOwnerId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
