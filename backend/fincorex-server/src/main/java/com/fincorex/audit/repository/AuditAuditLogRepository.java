package com.fincorex.audit.repository;

import com.fincorex.audit.entity.AuditAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AuditAuditLogRepository extends JpaRepository<AuditAuditLogEntity, UUID> {
    List<AuditAuditLogEntity> findByRecordId(UUID recordId);
    List<AuditAuditLogEntity> findByActionType(String actionType);
    List<AuditAuditLogEntity> findByPerformedBy(String performedBy);
}
