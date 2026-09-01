package com.fincorex.gateway.exception;

/**
 * Specific Business Exception for Gateway Operations
 */
public class GatewayDomainException extends RuntimeException {
    private final String errorCode;

    public GatewayDomainException(String message) {
        super(message);
        this.errorCode = "GATEWAY_ERR_GENERAL";
    }

    public GatewayDomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
