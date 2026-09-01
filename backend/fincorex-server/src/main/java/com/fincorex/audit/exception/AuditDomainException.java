package com.fincorex.audit.exception;

/**
 * Specific Business Exception for Audit Operations
 */
public class AuditDomainException extends RuntimeException {
    private final String errorCode;

    public AuditDomainException(String message) {
        super(message);
        this.errorCode = "AUDIT_ERR_GENERAL";
    }

    public AuditDomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
