package com.fincorex.identity.repository;

import com.fincorex.identity.entity.IdentityAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IdentityAuditLogRepository extends JpaRepository<IdentityAuditLogEntity, UUID> {
    List<IdentityAuditLogEntity> findByRecordId(UUID recordId);
    List<IdentityAuditLogEntity> findByActionType(String actionType);
    List<IdentityAuditLogEntity> findByPerformedBy(String performedBy);
}
