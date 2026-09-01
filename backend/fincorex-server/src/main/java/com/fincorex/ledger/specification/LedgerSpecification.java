package com.fincorex.ledger.specification;

import com.fincorex.ledger.entity.LedgerRecordEntity;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Dynamic Specification Builder for Ledger Queries
 */
public class LedgerSpecification {

    public static Specification<LedgerRecordEntity> hasOwnerId(UUID ownerId) {
        return (root, query, cb) -> ownerId == null ? null : cb.equal(root.get("ownerId"), ownerId);
    }

    public static Specification<LedgerRecordEntity> hasStatus(String status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<LedgerRecordEntity> amountGreaterThanOrEqual(BigDecimal minAmount) {
        return (root, query, cb) -> minAmount == null ? null : cb.greaterThanOrEqualTo(root.get("amount"), minAmount);
    }

    public static Specification<LedgerRecordEntity> createdAfter(LocalDateTime fromDate) {
        return (root, query, cb) -> fromDate == null ? null : cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate);
    }
}
