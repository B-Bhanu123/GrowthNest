package com.fincorex.investment.resilience;

import com.fincorex.investment.dto.InvestmentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Resilience4j Circuit Breaker Fallback Handler for Investment & Portfolio Platform
 */
@Component
public class InvestmentCircuitBreakerFallback {

    private static final Logger log = LoggerFactory.getLogger(InvestmentCircuitBreakerFallback.class);

    public InvestmentDTO fallbackForCreate(String referenceCode, UUID ownerId, BigDecimal amount, String status, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Fallback triggered for Investment & Portfolio Platform ref: {} due to: {}", referenceCode, t.getMessage());

        InvestmentDTO fallback = new InvestmentDTO();
        fallback.setId(UUID.randomUUID());
        fallback.setReferenceCode(referenceCode);
        fallback.setOwnerId(ownerId);
        fallback.setAmount(amount);
        fallback.setStatus("FALLBACK_DEGRADED");
        fallback.setCreatedAt(LocalDateTime.now());
        return fallback;
    }

    public InvestmentDTO fallbackForRead(String referenceCode, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Read fallback triggered for Investment & Portfolio Platform ref: {}", referenceCode, t);
        return new InvestmentDTO(UUID.randomUUID(), referenceCode, UUID.randomUUID(), BigDecimal.ZERO, "CACHE_FALLBACK", LocalDateTime.now());
    }
}
