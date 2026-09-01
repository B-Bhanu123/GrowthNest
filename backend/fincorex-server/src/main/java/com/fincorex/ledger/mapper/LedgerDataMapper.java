package com.fincorex.ledger.mapper;

import com.fincorex.ledger.dto.LedgerDTO;
import com.fincorex.ledger.entity.LedgerRecordEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MapStruct / ModelMapper equivalent converter for Double-Entry Financial Ledger
 */
@Component
public class LedgerDataMapper {

    public LedgerDTO toDTO(LedgerRecordEntity entity) {
        if (entity == null) return null;
        LedgerDTO dto = new LedgerDTO();
        dto.setId(entity.getId());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setOwnerId(entity.getOwnerId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public LedgerRecordEntity toEntity(LedgerDTO dto) {
        if (dto == null) return null;
        LedgerRecordEntity entity = new LedgerRecordEntity();
        entity.setId(dto.getId());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setOwnerId(dto.getOwnerId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
