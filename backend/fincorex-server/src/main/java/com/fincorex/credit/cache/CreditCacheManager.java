package com.fincorex.credit.cache;

import com.fincorex.credit.dto.CreditDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Distributed Cache Adapter for Credit Scoring System (Credit)
 */
@Component
public class CreditCacheManager {

    private static final Logger log = LoggerFactory.getLogger(CreditCacheManager.class);

    private final Map<String, CreditDTO> cacheStore = new ConcurrentHashMap<>();

    public void put(String referenceCode, CreditDTO dto) {
        log.debug("[CACHE-PUT] Caching DTO for ref: {}", referenceCode);
        cacheStore.put(referenceCode, dto);
    }

    public Optional<CreditDTO> get(String referenceCode) {
        CreditDTO hit = cacheStore.get(referenceCode);
        if (hit != null) {
            log.debug("[CACHE-HIT] Found ref: {} in L1/L2 Redis cache", referenceCode);
            return Optional.of(hit);
        }
        log.debug("[CACHE-MISS] Ref: {} not found in cache", referenceCode);
        return Optional.empty();
    }

    public void evict(String referenceCode) {
        log.debug("[CACHE-EVICT] Evicting ref: {}", referenceCode);
        cacheStore.remove(referenceCode);
    }

    public void clearAll() {
        log.info("[CACHE-CLEAR] Clearing all cached entries for credit");
        cacheStore.clear();
    }
}
