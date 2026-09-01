package com.fincorex.wallet.repository;

import com.fincorex.wallet.entity.WalletAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WalletAuditLogRepository extends JpaRepository<WalletAuditLogEntity, UUID> {
    List<WalletAuditLogEntity> findByRecordId(UUID recordId);
    List<WalletAuditLogEntity> findByActionType(String actionType);
    List<WalletAuditLogEntity> findByPerformedBy(String performedBy);
}
