package com.fincorex.analytics.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Enterprise Sub-Domain Relationship Entity for Financial Analytics Engine (Analytics)
 */
@Entity
@Table(name = "analytics_sub_records", schema = "analytics")
public class AnalyticsSubRecordEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID subRecordId;

    @Column(name = "parent_record_id", nullable = false)
    private UUID parentRecordId;

    @Column(name = "sub_type", nullable = false, length = 100)
    private String subType;

    @Column(name = "sub_value", precision = 18, scale = 4)
    private BigDecimal subValue;

    @Column(name = "status_flag", nullable = false, length = 50)
    private String statusFlag;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public AnalyticsSubRecordEntity() {
        this.createdAt = LocalDateTime.now();
    }

    public AnalyticsSubRecordEntity(UUID parentRecordId, String subType, BigDecimal subValue, String statusFlag) {
        this.parentRecordId = parentRecordId;
        this.subType = subType;
        this.subValue = subValue;
        this.statusFlag = statusFlag;
        this.createdAt = LocalDateTime.now();
    }

    public UUID getSubRecordId() { return subRecordId; }
    public void setSubRecordId(UUID subRecordId) { this.subRecordId = subRecordId; }

    public UUID getParentRecordId() { return parentRecordId; }
    public void setParentRecordId(UUID parentRecordId) { this.parentRecordId = parentRecordId; }

    public String getSubType() { return subType; }
    public void setSubType(String subType) { this.subType = subType; }

    public BigDecimal getSubValue() { return subValue; }
    public void setSubValue(BigDecimal subValue) { this.subValue = subValue; }

    public String getStatusFlag() { return statusFlag; }
    public void setStatusFlag(String statusFlag) { this.statusFlag = statusFlag; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
