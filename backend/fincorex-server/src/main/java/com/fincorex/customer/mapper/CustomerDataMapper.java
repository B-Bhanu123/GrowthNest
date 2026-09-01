package com.fincorex.customer.mapper;

import com.fincorex.customer.dto.CustomerDTO;
import com.fincorex.customer.entity.CustomerRecordEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MapStruct / ModelMapper equivalent converter for Customer & Account Management
 */
@Component
public class CustomerDataMapper {

    public CustomerDTO toDTO(CustomerRecordEntity entity) {
        if (entity == null) return null;
        CustomerDTO dto = new CustomerDTO();
        dto.setId(entity.getId());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setOwnerId(entity.getOwnerId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public CustomerRecordEntity toEntity(CustomerDTO dto) {
        if (dto == null) return null;
        CustomerRecordEntity entity = new CustomerRecordEntity();
        entity.setId(dto.getId());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setOwnerId(dto.getOwnerId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
