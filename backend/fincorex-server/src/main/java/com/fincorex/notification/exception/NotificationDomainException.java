package com.fincorex.notification.exception;

/**
 * Specific Business Exception for Notification Operations
 */
public class NotificationDomainException extends RuntimeException {
    private final String errorCode;

    public NotificationDomainException(String message) {
        super(message);
        this.errorCode = "NOTIFICATION_ERR_GENERAL";
    }

    public NotificationDomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
