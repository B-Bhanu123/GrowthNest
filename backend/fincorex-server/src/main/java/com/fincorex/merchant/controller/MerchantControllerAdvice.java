package com.fincorex.merchant.controller;

import com.fincorex.merchant.exception.MerchantDomainException;
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
 * Controller Advice for Merchant Acquiring Management REST Endpoints
 */
@RestControllerAdvice(basePackages = "com.fincorex.merchant.controller")
public class MerchantControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(MerchantControllerAdvice.class);

    @ExceptionHandler(MerchantDomainException.class)
    public ResponseEntity<Map<String, Object>> handleDomainException(MerchantDomainException ex) {
        log.error("[DOMAIN-ERROR] Merchant Acquiring Management error [{}]: {}", ex.getErrorCode(), ex.getMessage());
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("errorCode", ex.getErrorCode());
        body.put("message", ex.getMessage());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneralException(Exception ex) {
        log.error("[SYSTEM-ERROR] Unexpected error in Merchant Acquiring Management", ex);
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("errorCode", "MERCHANT_INTERNAL_SERVER_ERROR");
        body.put("message", "An unexpected internal server error occurred");
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
