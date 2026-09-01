package com.fincorex.lending.repository;

import com.fincorex.lending.entity.LendingAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface LendingAuditLogRepository extends JpaRepository<LendingAuditLogEntity, UUID> {
    List<LendingAuditLogEntity> findByRecordId(UUID recordId);
    List<LendingAuditLogEntity> findByActionType(String actionType);
    List<LendingAuditLogEntity> findByPerformedBy(String performedBy);
}
