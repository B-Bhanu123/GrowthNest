package com.fincorex.admin.resilience;

import com.fincorex.admin.dto.AdminDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Resilience4j Circuit Breaker Fallback Handler for Admin & Operations Center
 */
@Component
public class AdminCircuitBreakerFallback {

    private static final Logger log = LoggerFactory.getLogger(AdminCircuitBreakerFallback.class);

    public AdminDTO fallbackForCreate(String referenceCode, UUID ownerId, BigDecimal amount, String status, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Fallback triggered for Admin & Operations Center ref: {} due to: {}", referenceCode, t.getMessage());

        AdminDTO fallback = new AdminDTO();
        fallback.setId(UUID.randomUUID());
        fallback.setReferenceCode(referenceCode);
        fallback.setOwnerId(ownerId);
        fallback.setAmount(amount);
        fallback.setStatus("FALLBACK_DEGRADED");
        fallback.setCreatedAt(LocalDateTime.now());
        return fallback;
    }

    public AdminDTO fallbackForRead(String referenceCode, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Read fallback triggered for Admin & Operations Center ref: {}", referenceCode, t);
        return new AdminDTO(UUID.randomUUID(), referenceCode, UUID.randomUUID(), BigDecimal.ZERO, "CACHE_FALLBACK", LocalDateTime.now());
    }
}
