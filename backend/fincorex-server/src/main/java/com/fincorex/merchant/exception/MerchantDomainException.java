package com.fincorex.merchant.exception;

/**
 * Specific Business Exception for Merchant Operations
 */
public class MerchantDomainException extends RuntimeException {
    private final String errorCode;

    public MerchantDomainException(String message) {
        super(message);
        this.errorCode = "MERCHANT_ERR_GENERAL";
    }

    public MerchantDomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
