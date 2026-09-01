package com.fincorex.gateway.service;

import com.fincorex.gateway.dto.GatewayDTO;
import com.fincorex.gateway.entity.GatewayRecordEntity;
import com.fincorex.gateway.repository.GatewayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class GatewayService {

    private final GatewayRepository repository;

    @Autowired
    public GatewayService(GatewayRepository repository) {
        this.repository = repository;
    }

    public GatewayDTO createRecord(String referenceCode, UUID ownerId, BigDecimal amount, String status) {
        GatewayRecordEntity entity = new GatewayRecordEntity(referenceCode, ownerId, amount, status);
        GatewayRecordEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public GatewayDTO getByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Gateway record not found for ref: " + referenceCode));
    }

    @Transactional(readOnly = true)
    public List<GatewayDTO> getByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public GatewayDTO updateStatus(UUID id, String newStatus) {
        GatewayRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + id));
        entity.setStatus(newStatus);
        GatewayRecordEntity updated = repository.save(entity);
        return mapToDTO(updated);
    }

    private GatewayDTO mapToDTO(GatewayRecordEntity entity) {
        return new GatewayDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
