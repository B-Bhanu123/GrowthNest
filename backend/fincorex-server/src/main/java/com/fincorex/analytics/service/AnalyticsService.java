package com.fincorex.analytics.service;

import com.fincorex.analytics.dto.AnalyticsDTO;
import com.fincorex.analytics.entity.AnalyticsRecordEntity;
import com.fincorex.analytics.repository.AnalyticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AnalyticsService {

    private final AnalyticsRepository repository;

    @Autowired
    public AnalyticsService(AnalyticsRepository repository) {
        this.repository = repository;
    }

    public AnalyticsDTO createRecord(String referenceCode, UUID ownerId, BigDecimal amount, String status) {
        AnalyticsRecordEntity entity = new AnalyticsRecordEntity(referenceCode, ownerId, amount, status);
        AnalyticsRecordEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public AnalyticsDTO getByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Analytics record not found for ref: " + referenceCode));
    }

    @Transactional(readOnly = true)
    public List<AnalyticsDTO> getByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public AnalyticsDTO updateStatus(UUID id, String newStatus) {
        AnalyticsRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + id));
        entity.setStatus(newStatus);
        AnalyticsRecordEntity updated = repository.save(entity);
        return mapToDTO(updated);
    }

    private AnalyticsDTO mapToDTO(AnalyticsRecordEntity entity) {
        return new AnalyticsDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
