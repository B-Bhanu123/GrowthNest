package com.fincorex.accounting.mapper;

import com.fincorex.accounting.dto.AccountingDTO;
import com.fincorex.accounting.entity.AccountingRecordEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MapStruct / ModelMapper equivalent converter for General Accounting & Trial Balance
 */
@Component
public class AccountingDataMapper {

    public AccountingDTO toDTO(AccountingRecordEntity entity) {
        if (entity == null) return null;
        AccountingDTO dto = new AccountingDTO();
        dto.setId(entity.getId());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setOwnerId(entity.getOwnerId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public AccountingRecordEntity toEntity(AccountingDTO dto) {
        if (dto == null) return null;
        AccountingRecordEntity entity = new AccountingRecordEntity();
        entity.setId(dto.getId());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setOwnerId(dto.getOwnerId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
