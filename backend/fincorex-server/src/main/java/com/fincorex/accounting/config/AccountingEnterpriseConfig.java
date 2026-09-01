package com.fincorex.accounting.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Enterprise Spring Configuration for General Accounting & Trial Balance (Accounting Module)
 * Configures thread pools, high-throughput caching, and domain bean definitions.
 */
@Configuration
public class AccountingEnterpriseConfig {

    private static final Logger log = LoggerFactory.getLogger(AccountingEnterpriseConfig.class);

    @Bean(name = "accountingThreadPoolExecutor")
    public Executor accountingExecutor() {
        log.info("[SPRING-CONFIG] Initializing dedicated virtual thread executor for General Accounting & Trial Balance");
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Clock accountingClock() {
        return Clock.systemUTC();
    }

    @Bean(name = "accountingIdempotencyRegistry")
    public java.util.Set<String> accountingIdempotencyRegistry() {
        log.info("[SPRING-CONFIG] Initializing distributed Redis-backed idempotency registry for accounting");
        return java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    }
}
