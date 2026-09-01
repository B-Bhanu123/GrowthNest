package com.fincorex.insurance.repository;

import com.fincorex.insurance.entity.InsuranceAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InsuranceAuditLogRepository extends JpaRepository<InsuranceAuditLogEntity, UUID> {
    List<InsuranceAuditLogEntity> findByRecordId(UUID recordId);
    List<InsuranceAuditLogEntity> findByActionType(String actionType);
    List<InsuranceAuditLogEntity> findByPerformedBy(String performedBy);
}
