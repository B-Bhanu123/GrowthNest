package com.fincorex.settlement.repository;

import com.fincorex.settlement.entity.SettlementAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SettlementAuditLogRepository extends JpaRepository<SettlementAuditLogEntity, UUID> {
    List<SettlementAuditLogEntity> findByRecordId(UUID recordId);
    List<SettlementAuditLogEntity> findByActionType(String actionType);
    List<SettlementAuditLogEntity> findByPerformedBy(String performedBy);
}
