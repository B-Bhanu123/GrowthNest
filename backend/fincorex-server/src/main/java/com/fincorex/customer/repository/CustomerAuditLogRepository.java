package com.fincorex.customer.repository;

import com.fincorex.customer.entity.CustomerAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerAuditLogRepository extends JpaRepository<CustomerAuditLogEntity, UUID> {
    List<CustomerAuditLogEntity> findByRecordId(UUID recordId);
    List<CustomerAuditLogEntity> findByActionType(String actionType);
    List<CustomerAuditLogEntity> findByPerformedBy(String performedBy);
}
