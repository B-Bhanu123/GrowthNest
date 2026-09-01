package com.fincorex.expense.repository;

import com.fincorex.expense.entity.ExpenseAuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExpenseAuditLogRepository extends JpaRepository<ExpenseAuditLogEntity, UUID> {
    List<ExpenseAuditLogEntity> findByRecordId(UUID recordId);
    List<ExpenseAuditLogEntity> findByActionType(String actionType);
    List<ExpenseAuditLogEntity> findByPerformedBy(String performedBy);
}
