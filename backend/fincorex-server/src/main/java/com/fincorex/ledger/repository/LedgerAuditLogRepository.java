package com.fincorex.ledger.repository;

import com.fincorex.ledger.entity.LedgerAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LedgerAuditLogRepository extends JpaRepository<LedgerAuditLogEntity, UUID> {
    List<LedgerAuditLogEntity> findByRecordId(UUID recordId);
    List<LedgerAuditLogEntity> findByActionType(String actionType);
    List<LedgerAuditLogEntity> findByPerformedBy(String performedBy);
}
