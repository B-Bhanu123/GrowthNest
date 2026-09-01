package com.fincorex.payment.exception;

/**
 * Specific Business Exception for Payment Operations
 */
public class PaymentDomainException extends RuntimeException {
    private final String errorCode;

    public PaymentDomainException(String message) {
        super(message);
        this.errorCode = "PAYMENT_ERR_GENERAL";
    }

    public PaymentDomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
