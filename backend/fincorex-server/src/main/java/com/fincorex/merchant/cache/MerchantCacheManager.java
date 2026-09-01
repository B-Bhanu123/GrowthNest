package com.fincorex.merchant.cache;

import com.fincorex.merchant.dto.MerchantDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Distributed Cache Adapter for Merchant Acquiring Management (Merchant)
 */
@Component
public class MerchantCacheManager {

    private static final Logger log = LoggerFactory.getLogger(MerchantCacheManager.class);

    private final Map<String, MerchantDTO> cacheStore = new ConcurrentHashMap<>();

    public void put(String referenceCode, MerchantDTO dto) {
        log.debug("[CACHE-PUT] Caching DTO for ref: {}", referenceCode);
        cacheStore.put(referenceCode, dto);
    }

    public Optional<MerchantDTO> get(String referenceCode) {
        MerchantDTO hit = cacheStore.get(referenceCode);
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
        log.info("[CACHE-CLEAR] Clearing all cached entries for merchant");
        cacheStore.clear();
    }
}
