package com.fincorex.reconciliation.repository;

import com.fincorex.reconciliation.entity.ReconciliationSubRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReconciliationSubRecordRepository extends JpaRepository<ReconciliationSubRecordEntity, UUID> {
    List<ReconciliationSubRecordEntity> findByParentRecordId(UUID parentRecordId);
    List<ReconciliationSubRecordEntity> findBySubType(String subType);
}
