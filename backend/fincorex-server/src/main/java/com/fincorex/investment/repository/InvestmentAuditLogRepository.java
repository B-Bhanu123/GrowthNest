package com.fincorex.investment.repository;

import com.fincorex.investment.entity.InvestmentAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface InvestmentAuditLogRepository extends JpaRepository<InvestmentAuditLogEntity, UUID> {
    List<InvestmentAuditLogEntity> findByRecordId(UUID recordId);
    List<InvestmentAuditLogEntity> findByActionType(String actionType);
    List<InvestmentAuditLogEntity> findByPerformedBy(String performedBy);
}
