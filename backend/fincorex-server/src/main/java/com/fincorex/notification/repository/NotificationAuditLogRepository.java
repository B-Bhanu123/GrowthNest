package com.fincorex.notification.repository;

import com.fincorex.notification.entity.NotificationAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationAuditLogRepository extends JpaRepository<NotificationAuditLogEntity, UUID> {
    List<NotificationAuditLogEntity> findByRecordId(UUID recordId);
    List<NotificationAuditLogEntity> findByActionType(String actionType);
    List<NotificationAuditLogEntity> findByPerformedBy(String performedBy);
}
