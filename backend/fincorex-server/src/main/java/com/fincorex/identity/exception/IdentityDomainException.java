package com.fincorex.identity.exception;

/**
 * Specific Business Exception for Identity Operations
 */
public class IdentityDomainException extends RuntimeException {
    private final String errorCode;

    public IdentityDomainException(String message) {
        super(message);
        this.errorCode = "IDENTITY_ERR_GENERAL";
    }

    public IdentityDomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
