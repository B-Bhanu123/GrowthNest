package com.fincorex.credit.exception;

/**
 * Specific Business Exception for Credit Operations
 */
public class CreditDomainException extends RuntimeException {
    private final String errorCode;

    public CreditDomainException(String message) {
        super(message);
        this.errorCode = "CREDIT_ERR_GENERAL";
    }

    public CreditDomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
