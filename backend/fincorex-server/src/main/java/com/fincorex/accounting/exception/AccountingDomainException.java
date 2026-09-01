package com.fincorex.accounting.exception;

/**
 * Specific Business Exception for Accounting Operations
 */
public class AccountingDomainException extends RuntimeException {
    private final String errorCode;

    public AccountingDomainException(String message) {
        super(message);
        this.errorCode = "ACCOUNTING_ERR_GENERAL";
    }

    public AccountingDomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
