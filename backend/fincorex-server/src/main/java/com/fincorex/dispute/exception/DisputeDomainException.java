package com.fincorex.dispute.exception;

/**
 * Specific Business Exception for Dispute Operations
 */
public class DisputeDomainException extends RuntimeException {
    private final String errorCode;

    public DisputeDomainException(String message) {
        super(message);
        this.errorCode = "DISPUTE_ERR_GENERAL";
    }

    public DisputeDomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
