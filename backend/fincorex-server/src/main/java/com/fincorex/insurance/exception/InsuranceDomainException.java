package com.fincorex.insurance.exception;

/**
 * Specific Business Exception for Insurance Operations
 */
public class InsuranceDomainException extends RuntimeException {
    private final String errorCode;

    public InsuranceDomainException(String message) {
        super(message);
        this.errorCode = "INSURANCE_ERR_GENERAL";
    }

    public InsuranceDomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
