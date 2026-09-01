package com.fincorex.gateway.repository;

import com.fincorex.gateway.entity.GatewayAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface GatewayAuditLogRepository extends JpaRepository<GatewayAuditLogEntity, UUID> {
    List<GatewayAuditLogEntity> findByRecordId(UUID recordId);
    List<GatewayAuditLogEntity> findByActionType(String actionType);
    List<GatewayAuditLogEntity> findByPerformedBy(String performedBy);
}
