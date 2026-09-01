package com.fincorex.admin.exception;

/**
 * Specific Business Exception for Admin Operations
 */
public class AdminDomainException extends RuntimeException {
    private final String errorCode;

    public AdminDomainException(String message) {
        super(message);
        this.errorCode = "ADMIN_ERR_GENERAL";
    }

    public AdminDomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
