package com.fincorex.analytics.exception;

/**
 * Specific Business Exception for Analytics Operations
 */
public class AnalyticsDomainException extends RuntimeException {
    private final String errorCode;

    public AnalyticsDomainException(String message) {
        super(message);
        this.errorCode = "ANALYTICS_ERR_GENERAL";
    }

    public AnalyticsDomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
