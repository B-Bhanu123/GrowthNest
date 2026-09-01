package com.fincorex.insurance.mapper;

import com.fincorex.insurance.dto.InsuranceDTO;
import com.fincorex.insurance.entity.InsuranceRecordEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MapStruct / ModelMapper equivalent converter for Insurance Policy System
 */
@Component
public class InsuranceDataMapper {

    public InsuranceDTO toDTO(InsuranceRecordEntity entity) {
        if (entity == null) return null;
        InsuranceDTO dto = new InsuranceDTO();
        dto.setId(entity.getId());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setOwnerId(entity.getOwnerId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public InsuranceRecordEntity toEntity(InsuranceDTO dto) {
        if (dto == null) return null;
        InsuranceRecordEntity entity = new InsuranceRecordEntity();
        entity.setId(dto.getId());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setOwnerId(dto.getOwnerId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
