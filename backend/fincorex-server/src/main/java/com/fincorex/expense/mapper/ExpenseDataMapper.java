package com.fincorex.expense.mapper;

import com.fincorex.expense.dto.ExpenseDTO;
import com.fincorex.expense.entity.ExpenseRecordEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MapStruct / ModelMapper equivalent converter for Corporate Expense Management
 */
@Component
public class ExpenseDataMapper {

    public ExpenseDTO toDTO(ExpenseRecordEntity entity) {
        if (entity == null) return null;
        ExpenseDTO dto = new ExpenseDTO();
        dto.setId(entity.getId());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setOwnerId(entity.getOwnerId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public ExpenseRecordEntity toEntity(ExpenseDTO dto) {
        if (dto == null) return null;
        ExpenseRecordEntity entity = new ExpenseRecordEntity();
        entity.setId(dto.getId());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setOwnerId(dto.getOwnerId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
