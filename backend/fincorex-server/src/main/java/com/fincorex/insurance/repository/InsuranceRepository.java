package com.fincorex.insurance.repository;

import com.fincorex.insurance.entity.InsuranceRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InsuranceRepository extends JpaRepository<InsuranceRecordEntity, UUID> {
    Optional<InsuranceRecordEntity> findByReferenceCode(String referenceCode);
    List<InsuranceRecordEntity> findByOwnerId(UUID ownerId);
    List<InsuranceRecordEntity> findByStatus(String status);
}
