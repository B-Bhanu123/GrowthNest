package com.fincorex.fraud.exception;

/**
 * Specific Business Exception for Fraud Operations
 */
public class FraudDomainException extends RuntimeException {
    private final String errorCode;

    public FraudDomainException(String message) {
        super(message);
        this.errorCode = "FRAUD_ERR_GENERAL";
    }

    public FraudDomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
