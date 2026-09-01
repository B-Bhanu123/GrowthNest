package com.fincorex.payment.resilience;

import com.fincorex.payment.dto.PaymentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Resilience4j Circuit Breaker Fallback Handler for Payment Gateway Orchestration
 */
@Component
public class PaymentCircuitBreakerFallback {

    private static final Logger log = LoggerFactory.getLogger(PaymentCircuitBreakerFallback.class);

    public PaymentDTO fallbackForCreate(String referenceCode, UUID ownerId, BigDecimal amount, String status, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Fallback triggered for Payment Gateway Orchestration ref: {} due to: {}", referenceCode, t.getMessage());

        PaymentDTO fallback = new PaymentDTO();
        fallback.setId(UUID.randomUUID());
        fallback.setReferenceCode(referenceCode);
        fallback.setOwnerId(ownerId);
        fallback.setAmount(amount);
        fallback.setStatus("FALLBACK_DEGRADED");
        fallback.setCreatedAt(LocalDateTime.now());
        return fallback;
    }

    public PaymentDTO fallbackForRead(String referenceCode, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Read fallback triggered for Payment Gateway Orchestration ref: {}", referenceCode, t);
        return new PaymentDTO(UUID.randomUUID(), referenceCode, UUID.randomUUID(), BigDecimal.ZERO, "CACHE_FALLBACK", LocalDateTime.now());
    }
}
