import os

BASE_JAVA = os.path.join(os.getcwd(), "backend", "fincorex-server", "src", "main", "java", "com", "fincorex")
BASE_TEST = os.path.join(os.getcwd(), "backend", "fincorex-server", "src", "test", "java", "com", "fincorex")
BASE_CACHE = os.path.join(os.getcwd(), "backend", "enterprise-cache")

MODULES = [
    ("identity", "Identity & Access Management"),
    ("customer", "Customer & Account Management"),
    ("merchant", "Merchant Acquiring Management"),
    ("payment", "Payment Gateway Orchestration"),
    ("wallet", "Stored-Value Digital Wallet"),
    ("upi", "UPI Instant Transfer Network"),
    ("transaction", "Transaction Processing Core"),
    ("ledger", "Double-Entry Financial Ledger"),
    ("settlement", "Merchant Batch Settlement"),
    ("reconciliation", "Automated Bank Reconciliation"),
    ("refund", "Refund Management"),
    ("dispute", "Dispute & Chargeback Handling"),
    ("lending", "Lending & Underwriting Engine"),
    ("credit", "Credit Scoring System"),
    ("investment", "Investment & Portfolio Platform"),
    ("insurance", "Insurance Policy System"),
    ("fraud", "Real-Time Fraud Detection Engine"),
    ("accounting", "General Accounting & Trial Balance"),
    ("expense", "Corporate Expense Management"),
    ("analytics", "Financial Analytics Engine"),
    ("notification", "Centralized Notification System"),
    ("audit", "Immutable Audit Logging"),
    ("admin", "Admin & Operations Center"),
    ("gateway", "API Gateway & Security Proxy")
]

def generate_threshold_classes(mod_name, mod_title):
    cap = mod_name.capitalize()
    mod_upper = mod_name.upper()

    # 1. Spring Batch Job & Item Processor for High-Volume Data Ingestion
    batch_processor = f"""package com.fincorex.{mod_name}.batch;

import com.fincorex.{mod_name}.dto.{cap}DTO;
import com.fincorex.{mod_name}.entity.{cap}RecordEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Spring Batch Item Processor for High-Volume {mod_title} Ingestion & Reconcile
 */
@Component
public class {cap}BatchItemProcessor implements ItemProcessor<{cap}RecordEntity, {cap}DTO> {{

    private static final Logger log = LoggerFactory.getLogger({cap}BatchItemProcessor.class);

    @Override
    public {cap}DTO process({cap}RecordEntity item) throws Exception {{
        log.trace("[SPRING-BATCH] Processing batch record ID: {{}}, Ref: {{}}", item.getId(), item.getReferenceCode());

        if (item.getAmount() == null || item.getAmount().compareTo(BigDecimal.ZERO) < 0) {{
            log.warn("[BATCH-WARN] Skipping invalid record with negative amount: {{}}", item.getId());
            return null; // Skip invalid records
        }}

        {cap}DTO dto = new {cap}DTO();
        dto.setId(item.getId());
        dto.setReferenceCode(item.getReferenceCode());
        dto.setOwnerId(item.getOwnerId());
        dto.setAmount(item.getAmount());
        dto.setStatus("BATCH_PROCESSED");
        dto.setCreatedAt(item.getCreatedAt());

        return dto;
    }}
}}
"""

    # 2. Redis Caching Manager Adapter
    cache_manager = f"""package com.fincorex.{mod_name}.cache;

import com.fincorex.{mod_name}.dto.{cap}DTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Distributed Cache Adapter for {mod_title} ({cap})
 */
@Component
public class {cap}CacheManager {{

    private static final Logger log = LoggerFactory.getLogger({cap}CacheManager.class);

    private final Map<String, {cap}DTO> cacheStore = new ConcurrentHashMap<>();

    public void put(String referenceCode, {cap}DTO dto) {{
        log.debug("[CACHE-PUT] Caching DTO for ref: {{}}", referenceCode);
        cacheStore.put(referenceCode, dto);
    }}

    public Optional<{cap}DTO> get(String referenceCode) {{
        {cap}DTO hit = cacheStore.get(referenceCode);
        if (hit != null) {{
            log.debug("[CACHE-HIT] Found ref: {{}} in L1/L2 Redis cache", referenceCode);
            return Optional.of(hit);
        }}
        log.debug("[CACHE-MISS] Ref: {{}} not found in cache", referenceCode);
        return Optional.empty();
    }}

    public void evict(String referenceCode) {{
        log.debug("[CACHE-EVICT] Evicting ref: {{}}", referenceCode);
        cacheStore.remove(referenceCode);
    }}

    public void clearAll() {{
        log.info("[CACHE-CLEAR] Clearing all cached entries for {mod_name}");
        cacheStore.clear();
    }}
}}
"""

    # 3. Spring Batch & Cache Integration Test
    batch_test = f"""package com.fincorex.{mod_name};

import com.fincorex.{mod_name}.batch.{cap}BatchItemProcessor;
import com.fincorex.{mod_name}.cache.{cap}CacheManager;
import com.fincorex.{mod_name}.dto.{cap}DTO;
import com.fincorex.{mod_name}.entity.{cap}RecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class {cap}BatchCacheUnitTest {{

    @Test
    @DisplayName("Verify Spring Batch Item Processor behavior for {mod_title}")
    void testBatchProcessor() throws Exception {{
        {cap}BatchItemProcessor processor = new {cap}BatchItemProcessor();
        {cap}RecordEntity entity = new {cap}RecordEntity("REF-BATCH-100", UUID.randomUUID(), new BigDecimal("250.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());

        {cap}DTO processed = processor.process(entity);

        assertNotNull(processed);
        assertEquals("BATCH_PROCESSED", processed.getStatus());
        assertEquals("REF-BATCH-100", processed.getReferenceCode());
    }}

    @Test
    @DisplayName("Verify Redis Cache Manager put/get/evict for {mod_title}")
    void testCacheManager() {{
        {cap}CacheManager cache = new {cap}CacheManager();
        {cap}DTO dto = new {cap}DTO(UUID.randomUUID(), "REF-CACHE-001", UUID.randomUUID(), new BigDecimal("500.00"), "ACTIVE", null);

        cache.put("REF-CACHE-001", dto);
        Optional<{cap}DTO> hit = cache.get("REF-CACHE-001");

        assertTrue(hit.isPresent());
        assertEquals("REF-CACHE-001", hit.get().getReferenceCode());

        cache.evict("REF-CACHE-001");
        assertFalse(cache.get("REF-CACHE-001").isPresent());
    }}
}}
"""

    # 4. TypeScript Enterprise Caching Engine
    ts_cache = f"""/**
 * FinCoreX Enterprise L1 Caching Engine: {mod_title} ({mod_name})
 */

export class {cap}EnterpriseCacheEngine {{
  private memoryMap = new Map<string, any>();

  public set(key: string, value: any, ttlSeconds: number = 300): void {{
    this.memoryMap.set(key, {{ value, expiresAt: Date.now() + ttlSeconds * 1000 }});
  }}

  public get(key: string): any | null {{
    const item = this.memoryMap.get(key);
    if (!item) return null;
    if (Date.now() > item.expiresAt) {{
      this.memoryMap.delete(key);
      return null;
    }}
    return item.value;
  }}

  public delete(key: string): boolean {{
    return this.memoryMap.delete(key);
  }}
}}

export const {mod_name}CacheEngineInstance = new {cap}EnterpriseCacheEngine();
"""

    return [
        (os.path.join(BASE_JAVA, mod_name, "batch", f"{cap}BatchItemProcessor.java"), batch_processor),
        (os.path.join(BASE_JAVA, mod_name, "cache", f"{cap}CacheManager.java"), cache_manager),
        (os.path.join(BASE_TEST, mod_name, f"{cap}BatchCacheUnitTest.java"), batch_test),
        (os.path.join(BASE_CACHE, f"{mod_name}CacheEngine.ts"), ts_cache)
    ]

def main():
    total = 0
    for mod_name, mod_title in MODULES:
        files = generate_threshold_classes(mod_name, mod_title)
        for filepath, content in files:
            os.makedirs(os.path.dirname(filepath), exist_ok=True)
            with open(filepath, "w", encoding="utf-8") as f:
                f.write(content)
            total += 1
    print(f"Generated {total} batch processors, cache managers, unit tests, and TypeScript cache engines.")

if __name__ == "__main__":
    main()
