package com.fincorex.reconciliation.exception;

/**
 * Specific Business Exception for Reconciliation Operations
 */
public class ReconciliationDomainException extends RuntimeException {
    private final String errorCode;

    public ReconciliationDomainException(String message) {
        super(message);
        this.errorCode = "RECONCILIATION_ERR_GENERAL";
    }

    public ReconciliationDomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
