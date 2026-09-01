package com.fincorex.expense.repository;

import com.fincorex.expense.entity.ExpenseRecordEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpenseRepository extends JpaRepository<ExpenseRecordEntity, UUID> {
    Optional<ExpenseRecordEntity> findByReferenceCode(String referenceCode);
    List<ExpenseRecordEntity> findByOwnerId(UUID ownerId);
    List<ExpenseRecordEntity> findByStatus(String status);
}
