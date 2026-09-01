package com.fincorex.lending.specification;

import com.fincorex.lending.entity.LendingRecordEntity;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Dynamic Specification Builder for Lending Queries
 */
public class LendingSpecification {

    public static Specification<LendingRecordEntity> hasOwnerId(UUID ownerId) {
        return (root, query, cb) -> ownerId == null ? null : cb.equal(root.get("ownerId"), ownerId);
    }

    public static Specification<LendingRecordEntity> hasStatus(String status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<LendingRecordEntity> amountGreaterThanOrEqual(BigDecimal minAmount) {
        return (root, query, cb) -> minAmount == null ? null : cb.greaterThanOrEqualTo(root.get("amount"), minAmount);
    }

    public static Specification<LendingRecordEntity> createdAfter(LocalDateTime fromDate) {
        return (root, query, cb) -> fromDate == null ? null : cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate);
    }
}
