package com.fincorex.admin.repository;

import com.fincorex.admin.entity.AdminAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdminAuditLogRepository extends JpaRepository<AdminAuditLogEntity, UUID> {
    List<AdminAuditLogEntity> findByRecordId(UUID recordId);
    List<AdminAuditLogEntity> findByActionType(String actionType);
    List<AdminAuditLogEntity> findByPerformedBy(String performedBy);
}
