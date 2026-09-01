package com.fincorex.transaction.controller;

import com.fincorex.transaction.exception.TransactionDomainException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller Advice for Transaction Processing Core REST Endpoints
 */
@RestControllerAdvice(basePackages = "com.fincorex.transaction.controller")
public class TransactionControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(TransactionControllerAdvice.class);

    @ExceptionHandler(TransactionDomainException.class)
    public ResponseEntity<Map<String, Object>> handleDomainException(TransactionDomainException ex) {
        log.error("[DOMAIN-ERROR] Transaction Processing Core error [{}]: {}", ex.getErrorCode(), ex.getMessage());
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("errorCode", ex.getErrorCode());
        body.put("message", ex.getMessage());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        log.error("[SYSTEM-ERROR] Unexpected error in Transaction Processing Core", ex);
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("errorCode", "TRANSACTION_INTERNAL_SERVER_ERROR");
        body.put("message", "An unexpected internal server error occurred");
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
