package com.fincorex.expense.exception;

/**
 * Specific Business Exception for Expense Operations
 */
public class ExpenseDomainException extends RuntimeException {
    private final String errorCode;

    public ExpenseDomainException(String message) {
        super(message);
        this.errorCode = "EXPENSE_ERR_GENERAL";
    }

    public ExpenseDomainException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() { return errorCode; }
}
