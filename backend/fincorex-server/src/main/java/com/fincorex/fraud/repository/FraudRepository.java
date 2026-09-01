package com.fincorex.fraud.repository;

import com.fincorex.fraud.entity.FraudRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FraudRepository extends JpaRepository<FraudRecordEntity, UUID> {
    Optional<FraudRecordEntity> findByReferenceCode(String referenceCode);
    List<FraudRecordEntity> findByOwnerId(UUID ownerId);
    List<FraudRecordEntity> findByStatus(String status);
}
