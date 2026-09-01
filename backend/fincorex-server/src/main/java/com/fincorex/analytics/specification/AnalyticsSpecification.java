package com.fincorex.analytics.specification;

import com.fincorex.analytics.entity.AnalyticsRecordEntity;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Dynamic Specification Builder for Analytics Queries
 */
public class AnalyticsSpecification {

    public static Specification<AnalyticsRecordEntity> hasOwnerId(UUID ownerId) {
        return (root, query, cb) -> ownerId == null ? null : cb.equal(root.get("ownerId"), ownerId);
    }

    public static Specification<AnalyticsRecordEntity> hasStatus(String status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    public static Specification<AnalyticsRecordEntity> amountGreaterThanOrEqual(BigDecimal minAmount) {
        return (root, query, cb) -> minAmount == null ? null : cb.greaterThanOrEqualTo(root.get("amount"), minAmount);
    }

    public static Specification<AnalyticsRecordEntity> createdAfter(LocalDateTime fromDate) {
        return (root, query, cb) -> fromDate == null ? null : cb.greaterThanOrEqualTo(root.get("createdAt"), fromDate);
    }
}
