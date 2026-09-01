package com.fincorex.credit.repository;

import com.fincorex.credit.entity.CreditAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CreditAuditLogRepository extends JpaRepository<CreditAuditLogEntity, UUID> {
    List<CreditAuditLogEntity> findByRecordId(UUID recordId);
    List<CreditAuditLogEntity> findByActionType(String actionType);
    List<CreditAuditLogEntity> findByPerformedBy(String performedBy);
}
