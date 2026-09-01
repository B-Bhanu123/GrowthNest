package com.fincorex.gateway.mapper;

import com.fincorex.gateway.dto.GatewayDTO;
import com.fincorex.gateway.entity.GatewayRecordEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MapStruct / ModelMapper equivalent converter for API Gateway & Security Proxy
 */
@Component
public class GatewayDataMapper {

    public GatewayDTO toDTO(GatewayRecordEntity entity) {
        if (entity == null) return null;
        GatewayDTO dto = new GatewayDTO();
        dto.setId(entity.getId());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setOwnerId(entity.getOwnerId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public GatewayRecordEntity toEntity(GatewayDTO dto) {
        if (dto == null) return null;
        GatewayRecordEntity entity = new GatewayRecordEntity();
        entity.setId(dto.getId());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setOwnerId(dto.getOwnerId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
