package com.fincorex.wallet.exception;

/**
 * Specific Business Exception for Wallet Operations
 */
public class WalletDomainException extends RuntimeException {
    private final String errorCode;

    public WalletDomainException(String message) {
        super(message);
        this.errorCode = "WALLET_ERR_GENERAL";
    }

    public WalletDomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
