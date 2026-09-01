package com.fincorex.notification.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Enterprise Create Request DTO for Notification Domain
 */
public class CreateNotificationRequest {

    @NotBlank(message = "Reference code cannot be blank")
    @Size(min = 4, max = 100, message = "Reference code must be between 4 and 100 characters")
    private String referenceCode;

    @NotNull(message = "Owner UUID is required")
    private UUID ownerId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.00", message = "Amount cannot be negative")
    private BigDecimal amount;

    @NotBlank(message = "Currency code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be a 3-letter ISO code")
    private String currency;

    private String category;
    private String notes;

    public CreateNotificationRequest() {}

    public CreateNotificationRequest(String referenceCode, UUID ownerId, BigDecimal amount, String currency) {
        this.referenceCode = referenceCode;
        this.ownerId = ownerId;
        this.amount = amount;
        this.currency = currency;
    }

    public String getReferenceCode() { return referenceCode; }
    public void setReferenceCode(String referenceCode) { this.referenceCode = referenceCode; }

    public UUID getOwnerId() { return ownerId; }
    public void setOwnerId(UUID ownerId) { this.ownerId = ownerId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
