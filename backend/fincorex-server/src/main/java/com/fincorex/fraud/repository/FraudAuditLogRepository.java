package com.fincorex.fraud.repository;

import com.fincorex.fraud.entity.FraudAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FraudAuditLogRepository extends JpaRepository<FraudAuditLogEntity, UUID> {
    List<FraudAuditLogEntity> findByRecordId(UUID recordId);
    List<FraudAuditLogEntity> findByActionType(String actionType);
    List<FraudAuditLogEntity> findByPerformedBy(String performedBy);
}
