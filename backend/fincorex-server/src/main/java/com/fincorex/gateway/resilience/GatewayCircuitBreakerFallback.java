package com.fincorex.gateway.resilience;

import com.fincorex.gateway.dto.GatewayDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Resilience4j Circuit Breaker Fallback Handler for API Gateway & Security Proxy
 */
@Component
public class GatewayCircuitBreakerFallback {

    private static final Logger log = LoggerFactory.getLogger(GatewayCircuitBreakerFallback.class);

    public GatewayDTO fallbackForCreate(String referenceCode, UUID ownerId, BigDecimal amount, String status, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Fallback triggered for API Gateway & Security Proxy ref: {} due to: {}", referenceCode, t.getMessage());

        GatewayDTO fallback = new GatewayDTO();
        fallback.setId(UUID.randomUUID());
        fallback.setReferenceCode(referenceCode);
        fallback.setOwnerId(ownerId);
        fallback.setAmount(amount);
        fallback.setStatus("FALLBACK_DEGRADED");
        fallback.setCreatedAt(LocalDateTime.now());
        return fallback;
    }

    public GatewayDTO fallbackForRead(String referenceCode, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Read fallback triggered for API Gateway & Security Proxy ref: {}", referenceCode, t);
        return new GatewayDTO(UUID.randomUUID(), referenceCode, UUID.randomUUID(), BigDecimal.ZERO, "CACHE_FALLBACK", LocalDateTime.now());
    }
}
