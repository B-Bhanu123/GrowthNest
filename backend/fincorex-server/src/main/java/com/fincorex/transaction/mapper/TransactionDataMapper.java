package com.fincorex.transaction.mapper;

import com.fincorex.transaction.dto.TransactionDTO;
import com.fincorex.transaction.entity.TransactionRecordEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MapStruct / ModelMapper equivalent converter for Transaction Processing Core
 */
@Component
public class TransactionDataMapper {

    public TransactionDTO toDTO(TransactionRecordEntity entity) {
        if (entity == null) return null;
        TransactionDTO dto = new TransactionDTO();
        dto.setId(entity.getId());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setOwnerId(entity.getOwnerId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public TransactionRecordEntity toEntity(TransactionDTO dto) {
        if (dto == null) return null;
        TransactionRecordEntity entity = new TransactionRecordEntity();
        entity.setId(dto.getId());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setOwnerId(dto.getOwnerId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
