package com.fincorex.reconciliation.repository;

import com.fincorex.reconciliation.entity.ReconciliationRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReconciliationRepository extends JpaRepository<ReconciliationRecordEntity, UUID> {
    Optional<ReconciliationRecordEntity> findByReferenceCode(String referenceCode);
    List<ReconciliationRecordEntity> findByOwnerId(UUID ownerId);
    List<ReconciliationRecordEntity> findByStatus(String status);
}
