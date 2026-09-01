package com.fincorex.merchant.resilience;

import com.fincorex.merchant.dto.MerchantDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Resilience4j Circuit Breaker Fallback Handler for Merchant Acquiring Management
 */
@Component
public class MerchantCircuitBreakerFallback {

    private static final Logger log = LoggerFactory.getLogger(MerchantCircuitBreakerFallback.class);

    public MerchantDTO fallbackForCreate(String referenceCode, UUID ownerId, BigDecimal amount, String status, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Fallback triggered for Merchant Acquiring Management ref: {} due to: {}", referenceCode, t.getMessage());

        MerchantDTO fallback = new MerchantDTO();
        fallback.setId(UUID.randomUUID());
        fallback.setReferenceCode(referenceCode);
        fallback.setOwnerId(ownerId);
        fallback.setAmount(amount);
        fallback.setStatus("FALLBACK_DEGRADED");
        fallback.setCreatedAt(LocalDateTime.now());
        return fallback;
    }

    public MerchantDTO fallbackForRead(String referenceCode, Throwable t) {
        log.error("[CIRCUIT-BREAKER] Read fallback triggered for Merchant Acquiring Management ref: {}", referenceCode, t);
        return new MerchantDTO(UUID.randomUUID(), referenceCode, UUID.randomUUID(), BigDecimal.ZERO, "CACHE_FALLBACK", LocalDateTime.now());
    }
}
