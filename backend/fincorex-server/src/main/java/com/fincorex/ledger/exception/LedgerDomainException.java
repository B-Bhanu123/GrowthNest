package com.fincorex.ledger.exception;

/**
 * Specific Business Exception for Ledger Operations
 */
public class LedgerDomainException extends RuntimeException {
    private final String errorCode;

    public LedgerDomainException(String message) {
        super(message);
        this.errorCode = "LEDGER_ERR_GENERAL";
    }

    public LedgerDomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
