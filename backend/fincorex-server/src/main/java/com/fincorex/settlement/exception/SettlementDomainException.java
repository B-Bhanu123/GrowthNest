package com.fincorex.settlement.exception;

/**
 * Specific Business Exception for Settlement Operations
 */
public class SettlementDomainException extends RuntimeException {
    private final String errorCode;

    public SettlementDomainException(String message) {
        super(message);
        this.errorCode = "SETTLEMENT_ERR_GENERAL";
    }

    public SettlementDomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
