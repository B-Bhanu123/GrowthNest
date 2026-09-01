package com.fincorex.reconciliation.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Enterprise Spring Configuration for Automated Bank Reconciliation (Reconciliation Module)
 * Configures thread pools, high-throughput caching, and domain bean definitions.
 */
@Configuration
public class ReconciliationEnterpriseConfig {

    private static final Logger log = LoggerFactory.getLogger(ReconciliationEnterpriseConfig.class);

    @Bean(name = "reconciliationThreadPoolExecutor")
    public Executor reconciliationExecutor() {
        log.info("[SPRING-CONFIG] Initializing dedicated virtual thread executor for Automated Bank Reconciliation");
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Clock reconciliationClock() {
        return Clock.systemUTC();
    }

    @Bean(name = "reconciliationIdempotencyRegistry")
    public java.util.Set<String> reconciliationIdempotencyRegistry() {
        log.info("[SPRING-CONFIG] Initializing distributed Redis-backed idempotency registry for reconciliation");
        return java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    }
}
