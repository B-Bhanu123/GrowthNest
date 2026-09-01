package com.fincorex.expense.specification;

import com.fincorex.expense.entity.ExpenseRecordEntity;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Dynamic Specification Builder for Expense Queries
 */
public class ExpenseSpecification {

    public static Specification<ExpenseRecordEntity> hasOwnerId(UUID ownerId) {
        return (root, query, cb) -> ownerId == null ? null : cb.equal(root.get("ownerId"), ownerId);
    }

    public static Specification<ExpenseRecordEntity> hasStatus(String status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<ExpenseRecordEntity> amountGreaterThanOrEqual(BigDecimal minAmount) {
        return (root, query, cb) -> minAmount == null ? null : cb.greaterThanOrEqualTo(root.get("amount"), minAmount);
    }

    public static Specification<ExpenseRecordEntity> createdAfter(LocalDateTime fromDate) {
        return (root, query, cb) -> fromDate == null ? null : cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate);
    }
}
