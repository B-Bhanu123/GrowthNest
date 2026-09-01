package com.fincorex.notification.service;

import com.fincorex.notification.dto.NotificationDTO;
import com.fincorex.notification.entity.NotificationRecordEntity;
import com.fincorex.notification.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository repository;

    @Autowired
    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public NotificationDTO createRecord(String referenceCode, UUID ownerId, BigDecimal amount, String status) {
        NotificationRecordEntity entity = new NotificationRecordEntity(referenceCode, ownerId, amount, status);
        NotificationRecordEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public NotificationDTO getByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Notification record not found for ref: " + referenceCode));
    }

    @Transactional(readOnly = true)
    public List<NotificationDTO> getByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public NotificationDTO updateStatus(UUID id, String newStatus) {
        NotificationRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + id));
        entity.setStatus(newStatus);
        NotificationRecordEntity updated = repository.save(entity);
        return mapToDTO(updated);
    }

    private NotificationDTO mapToDTO(NotificationRecordEntity entity) {
        return new NotificationDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
