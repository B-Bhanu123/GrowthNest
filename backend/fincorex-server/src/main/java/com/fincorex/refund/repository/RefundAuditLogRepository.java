package com.fincorex.refund.repository;

import com.fincorex.refund.entity.RefundAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RefundAuditLogRepository extends JpaRepository<RefundAuditLogEntity, UUID> {
    List<RefundAuditLogEntity> findByRecordId(UUID recordId);
    List<RefundAuditLogEntity> findByActionType(String actionType);
    List<RefundAuditLogEntity> findByPerformedBy(String performedBy);
}
