package com.fincorex.refund.exception;

/**
 * Specific Business Exception for Refund Operations
 */
public class RefundDomainException extends RuntimeException {
    private final String errorCode;

    public RefundDomainException(String message) {
        super(message);
        this.errorCode = "REFUND_ERR_GENERAL";
    }

    public RefundDomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
