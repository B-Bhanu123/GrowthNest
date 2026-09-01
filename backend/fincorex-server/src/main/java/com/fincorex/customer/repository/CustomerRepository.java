package com.fincorex.customer.repository;

import com.fincorex.customer.entity.CustomerRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerRecordEntity, UUID> {
    Optional<CustomerRecordEntity> findByReferenceCode(String referenceCode);
    List<CustomerRecordEntity> findByOwnerId(UUID ownerId);
    List<CustomerRecordEntity> findByStatus(String status);
}
