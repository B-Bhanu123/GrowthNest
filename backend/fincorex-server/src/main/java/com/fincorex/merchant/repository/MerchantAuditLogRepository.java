package com.fincorex.merchant.repository;

import com.fincorex.merchant.entity.MerchantAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MerchantAuditLogRepository extends JpaRepository<MerchantAuditLogEntity, UUID> {
    List<MerchantAuditLogEntity> findByRecordId(UUID recordId);
    List<MerchantAuditLogEntity> findByActionType(String actionType);
    List<MerchantAuditLogEntity> findByPerformedBy(String performedBy);
}
