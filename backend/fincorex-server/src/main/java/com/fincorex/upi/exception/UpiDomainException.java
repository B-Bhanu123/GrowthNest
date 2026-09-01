package com.fincorex.upi.exception;

/**
 * Specific Business Exception for Upi Operations
 */
public class UpiDomainException extends RuntimeException {
    private final String errorCode;

    public UpiDomainException(String message) {
        super(message);
        this.errorCode = "UPI_ERR_GENERAL";
    }

    public UpiDomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
