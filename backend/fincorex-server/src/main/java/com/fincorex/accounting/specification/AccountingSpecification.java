package com.fincorex.accounting.specification;

import com.fincorex.accounting.entity.AccountingRecordEntity;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Dynamic Specification Builder for Accounting Queries
 */
public class AccountingSpecification {

    public static Specification<AccountingRecordEntity> hasOwnerId(UUID ownerId) {
        return (root, query, cb) -> ownerId == null ? null : cb.equal(root.get("ownerId"), ownerId);
    }

    public static Specification<AccountingRecordEntity> hasStatus(String status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<AccountingRecordEntity> amountGreaterThanOrEqual(BigDecimal minAmount) {
        return (root, query, cb) -> minAmount == null ? null : cb.greaterThanOrEqualTo(root.get("amount"), minAmount);
    }

    public static Specification<AccountingRecordEntity> createdAfter(LocalDateTime fromDate) {
        return (root, query, cb) -> fromDate == null ? null : cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate);
    }
}
