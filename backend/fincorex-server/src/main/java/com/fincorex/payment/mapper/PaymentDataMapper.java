package com.fincorex.payment.mapper;

import com.fincorex.payment.dto.PaymentDTO;
import com.fincorex.payment.entity.PaymentRecordEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MapStruct / ModelMapper equivalent converter for Payment Gateway Orchestration
 */
@Component
public class PaymentDataMapper {

    public PaymentDTO toDTO(PaymentRecordEntity entity) {
        if (entity == null) return null;
        PaymentDTO dto = new PaymentDTO();
        dto.setId(entity.getId());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setOwnerId(entity.getOwnerId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public PaymentRecordEntity toEntity(PaymentDTO dto) {
        if (dto == null) return null;
        PaymentRecordEntity entity = new PaymentRecordEntity();
        entity.setId(dto.getId());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setOwnerId(dto.getOwnerId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
