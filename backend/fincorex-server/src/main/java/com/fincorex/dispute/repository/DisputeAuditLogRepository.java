package com.fincorex.dispute.repository;

import com.fincorex.dispute.entity.DisputeAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DisputeAuditLogRepository extends JpaRepository<DisputeAuditLogEntity, UUID> {
    List<DisputeAuditLogEntity> findByRecordId(UUID recordId);
    List<DisputeAuditLogEntity> findByActionType(String actionType);
    List<DisputeAuditLogEntity> findByPerformedBy(String performedBy);
}
