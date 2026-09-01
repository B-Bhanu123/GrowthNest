package com.fincorex.merchant.mapper;

import com.fincorex.merchant.dto.MerchantDTO;
import com.fincorex.merchant.entity.MerchantRecordEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MapStruct / ModelMapper equivalent converter for Merchant Acquiring Management
 */
@Component
public class MerchantDataMapper {

    public MerchantDTO toDTO(MerchantRecordEntity entity) {
        if (entity == null) return null;
        MerchantDTO dto = new MerchantDTO();
        dto.setId(entity.getId());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setOwnerId(entity.getOwnerId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public MerchantRecordEntity toEntity(MerchantDTO dto) {
        if (dto == null) return null;
        MerchantRecordEntity entity = new MerchantRecordEntity();
        entity.setId(dto.getId());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setOwnerId(dto.getOwnerId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
