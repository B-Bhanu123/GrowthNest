package com.fincorex.notification.mapper;

import com.fincorex.notification.dto.NotificationDTO;
import com.fincorex.notification.entity.NotificationRecordEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MapStruct / ModelMapper equivalent converter for Centralized Notification System
 */
@Component
public class NotificationDataMapper {

    public NotificationDTO toDTO(NotificationRecordEntity entity) {
        if (entity == null) return null;
        NotificationDTO dto = new NotificationDTO();
        dto.setId(entity.getId());
        dto.setReferenceCode(entity.getReferenceCode());
        dto.setOwnerId(entity.getOwnerId());
        dto.setAmount(entity.getAmount());
        dto.setStatus(entity.getStatus());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public NotificationRecordEntity toEntity(NotificationDTO dto) {
        if (dto == null) return null;
        NotificationRecordEntity entity = new NotificationRecordEntity();
        entity.setId(dto.getId());
        entity.setReferenceCode(dto.getReferenceCode());
        entity.setOwnerId(dto.getOwnerId());
        entity.setAmount(dto.getAmount());
        entity.setStatus(dto.getStatus());
        entity.setUpdatedAt(LocalDateTime.now());
        return entity;
    }
}
