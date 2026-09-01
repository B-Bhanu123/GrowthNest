package com.fincorex.upi.repository;

import com.fincorex.upi.entity.UpiAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UpiAuditLogRepository extends JpaRepository<UpiAuditLogEntity, UUID> {
    List<UpiAuditLogEntity> findByRecordId(UUID recordId);
    List<UpiAuditLogEntity> findByActionType(String actionType);
    List<UpiAuditLogEntity> findByPerformedBy(String performedBy);
}
