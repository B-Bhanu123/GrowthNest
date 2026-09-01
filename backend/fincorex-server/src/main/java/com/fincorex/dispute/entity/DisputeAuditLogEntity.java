package com.fincorex.dispute.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain Audit Log Entry Entity for Dispute & Chargeback Handling
 */
@Entity
@Table(name = "dispute_audit_logs", schema = "dispute")
public class DisputeAuditLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID auditId;

    @Column(name = "record_id", nullable = false)
    private UUID recordId;

    @Column(name = "action_type", nullable = false, length = 100)
    private String actionType;

    @Column(name = "performed_by", nullable = false)
    private String performedBy;

    @Column(name = "previous_state_json", columnDefinition = "TEXT")
    private String previousStateJson;

    @Column(name = "new_state_json", columnDefinition = "TEXT")
    private String newStateJson;

    @Column(name = "timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp;

    public DisputeAuditLogEntity() {
        this.timestamp = LocalDateTime.now();
    }

    public DisputeAuditLogEntity(UUID recordId, String actionType, String performedBy, String previousStateJson, String newStateJson) {
        this.recordId = recordId;
        this.actionType = actionType;
        this.performedBy = performedBy;
        this.previousStateJson = previousStateJson;
        this.newStateJson = newStateJson;
        this.timestamp = LocalDateTime.now();
    }

    public UUID getAuditId() { return auditId; }
    public void setAuditId(UUID auditId) { this.auditId = auditId; }

    public UUID getRecordId() { return recordId; }
    public void setRecordId(UUID recordId) { this.recordId = recordId; }

    public String getActionType() { return actionType; }
    public void setActionType(String actionType) { this.actionType = actionType; }

    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }

    public String getPreviousStateJson() { return previousStateJson; }
    public void setPreviousStateJson(String previousStateJson) { this.previousStateJson = previousStateJson; }

    public String getNewStateJson() { return newStateJson; }
    public void setNewStateJson(String newStateJson) { this.newStateJson = newStateJson; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
