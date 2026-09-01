package com.fincorex.transaction.repository;

import com.fincorex.transaction.entity.TransactionAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionAuditLogRepository extends JpaRepository<TransactionAuditLogEntity, UUID> {
    List<TransactionAuditLogEntity> findByRecordId(UUID recordId);
    List<TransactionAuditLogEntity> findByActionType(String actionType);
    List<TransactionAuditLogEntity> findByPerformedBy(String performedBy);
}
