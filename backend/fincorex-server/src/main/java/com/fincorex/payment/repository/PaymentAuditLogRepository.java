package com.fincorex.payment.repository;

import com.fincorex.payment.entity.PaymentAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentAuditLogRepository extends JpaRepository<PaymentAuditLogEntity, UUID> {
    List<PaymentAuditLogEntity> findByRecordId(UUID recordId);
    List<PaymentAuditLogEntity> findByActionType(String actionType);
    List<PaymentAuditLogEntity> findByPerformedBy(String performedBy);
}
