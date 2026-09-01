package com.fincorex.expense.repository;

import com.fincorex.expense.entity.ExpenseSubRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ExpenseSubRecordRepository extends JpaRepository<ExpenseSubRecordEntity, UUID> {
    List<ExpenseSubRecordEntity> findByParentRecordId(UUID parentRecordId);
    List<ExpenseSubRecordEntity> findBySubType(String subType);
}
