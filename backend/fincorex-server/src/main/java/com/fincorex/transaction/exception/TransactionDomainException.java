package com.fincorex.transaction.exception;

/**
 * Specific Business Exception for Transaction Operations
 */
public class TransactionDomainException extends RuntimeException {
    private final String errorCode;

    public TransactionDomainException(String message) {
        super(message);
        this.errorCode = "TRANSACTION_ERR_GENERAL";
    }

    public TransactionDomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
