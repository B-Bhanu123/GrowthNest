package com.fincorex.expense.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * FinCoreX Data Transfer Object for Expense Module
 */
public class ExpenseDTO {
    private UUID id;
    private String referenceCode;
    private UUID ownerId;
    private BigDecimal amount;
    private String status;
    private LocalDateTime createdAt;

    public ExpenseDTO() {}

    public ExpenseDTO(UUID id, String referenceCode, UUID ownerId, BigDecimal amount, String status, LocalDateTime createdAt) {
        this.id = id;
        this.referenceCode = referenceCode;
        this.ownerId = ownerId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getReferenceCode() { return referenceCode; }
    public void setReferenceCode(String referenceCode) { this.referenceCode = referenceCode; }

    public UUID getOwnerId() { return ownerId; }
    public void setOwnerId(UUID ownerId) { this.ownerId = ownerId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
