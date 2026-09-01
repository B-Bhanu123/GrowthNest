package com.fincorex.investment.mapper;

import com.fincorex.investment.dto.InvestmentDTO;
import com.fincorex.investment.entity.InvestmentRecordEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MapStruct / ModelMapper equivalent converter for Investment & Portfolio Platform
 */
@Component
public class InvestmentDataMapper {

    public InvestmentDTO toDTO(InvestmentRecordEntity entity) {
        if (entity == null) return null;
        InvestmentDTO dto = new InvestmentDTO();
        dto.setId(entity.getId());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setOwnerId(entity.getOwnerId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public InvestmentRecordEntity toEntity(InvestmentDTO dto) {
        if (dto == null) return null;
        InvestmentRecordEntity entity = new InvestmentRecordEntity();
        entity.setId(dto.getId());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setOwnerId(dto.getOwnerId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
