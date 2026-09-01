package com.fincorex.analytics.repository;

import com.fincorex.analytics.entity.AnalyticsAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AnalyticsAuditLogRepository extends JpaRepository<AnalyticsAuditLogEntity, UUID> {
    List<AnalyticsAuditLogEntity> findByRecordId(UUID recordId);
    List<AnalyticsAuditLogEntity> findByActionType(String actionType);
    List<AnalyticsAuditLogEntity> findByPerformedBy(String performedBy);
}
