package com.fincorex.analytics.mapper;

import com.fincorex.analytics.dto.AnalyticsDTO;
import com.fincorex.analytics.entity.AnalyticsRecordEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MapStruct / ModelMapper equivalent converter for Financial Analytics Engine
 */
@Component
public class AnalyticsDataMapper {

    public AnalyticsDTO toDTO(AnalyticsRecordEntity entity) {
        if (entity == null) return null;
        AnalyticsDTO dto = new AnalyticsDTO();
        dto.setId(entity.getId());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setOwnerId(entity.getOwnerId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public AnalyticsRecordEntity toEntity(AnalyticsDTO dto) {
        if (dto == null) return null;
        AnalyticsRecordEntity entity = new AnalyticsRecordEntity();
        entity.setId(dto.getId());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setOwnerId(dto.getOwnerId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
