package com.fincorex.reconciliation.repository;

import com.fincorex.reconciliation.entity.ReconciliationAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReconciliationAuditLogRepository extends JpaRepository<ReconciliationAuditLogEntity, UUID> {
    List<ReconciliationAuditLogEntity> findByRecordId(UUID recordId);
    List<ReconciliationAuditLogEntity> findByActionType(String actionType);
    List<ReconciliationAuditLogEntity> findByPerformedBy(String performedBy);
}
