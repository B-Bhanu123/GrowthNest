package com.fincorex.lending.exception;

/**
 * Specific Business Exception for Lending Operations
 */
public class LendingDomainException extends RuntimeException {
    private final String errorCode;

    public LendingDomainException(String message) {
        super(message);
        this.errorCode = "LENDING_ERR_GENERAL";
    }

    public LendingDomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
