package com.fincorex.customer.service;

import com.fincorex.customer.dto.CustomerDTO;
import com.fincorex.customer.entity.CustomerRecordEntity;
import com.fincorex.customer.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerService {

    private final CustomerRepository repository;

    @Autowired
    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public CustomerDTO createRecord(String referenceCode, UUID ownerId, BigDecimal amount, String status) {
        CustomerRecordEntity entity = new CustomerRecordEntity(referenceCode, ownerId, amount, status);
        CustomerRecordEntity saved = repository.save(entity);
        return mapToDTO(saved);
    }

    @Transactional(readOnly = true)
    public CustomerDTO getByReferenceCode(String referenceCode) {
        return repository.findByReferenceCode(referenceCode)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Customer record not found for ref: " + referenceCode));
    }

    @Transactional(readOnly = true)
    public List<CustomerDTO> getByOwnerId(UUID ownerId) {
        return repository.findByOwnerId(ownerId).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public CustomerDTO updateStatus(UUID id, String newStatus) {
        CustomerRecordEntity entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + id));
        entity.setStatus(newStatus);
        CustomerRecordEntity updated = repository.save(entity);
        return mapToDTO(updated);
    }

    private CustomerDTO mapToDTO(CustomerRecordEntity entity) {
        return new CustomerDTO(
                entity.getId(),
                entity.getReferenceCode(),
                entity.getOwnerId(),
                entity.getAmount(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}
