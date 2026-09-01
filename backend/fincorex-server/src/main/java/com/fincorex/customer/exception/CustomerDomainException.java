package com.fincorex.customer.exception;

/**
 * Specific Business Exception for Customer Operations
 */
public class CustomerDomainException extends RuntimeException {
    private final String errorCode;

    public CustomerDomainException(String message) {
        super(message);
        this.errorCode = "CUSTOMER_ERR_GENERAL";
    }

    public CustomerDomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
