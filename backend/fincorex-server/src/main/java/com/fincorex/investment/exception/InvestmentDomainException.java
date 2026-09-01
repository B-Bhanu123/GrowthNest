package com.fincorex.investment.exception;

/**
 * Specific Business Exception for Investment Operations
 */
public class InvestmentDomainException extends RuntimeException {
    private final String errorCode;

    public InvestmentDomainException(String message) {
        super(message);
        this.errorCode = "INVESTMENT_ERR_GENERAL";
    }

    public InvestmentDomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
