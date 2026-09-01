package com.fincorex.accounting.repository;

import com.fincorex.accounting.entity.AccountingAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AccountingAuditLogRepository extends JpaRepository<AccountingAuditLogEntity, UUID> {
    List<AccountingAuditLogEntity> findByRecordId(UUID recordId);
    List<AccountingAuditLogEntity> findByActionType(String actionType);
    List<AccountingAuditLogEntity> findByPerformedBy(String performedBy);
}
