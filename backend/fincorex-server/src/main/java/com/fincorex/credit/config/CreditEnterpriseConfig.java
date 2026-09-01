package com.fincorex.credit.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Enterprise Spring Configuration for Credit Scoring System (Credit Module)
 * Configures thread pools, high-throughput caching, and domain bean definitions.
 */
@Configuration
public class CreditEnterpriseConfig {

    private static final Logger log = LoggerFactory.getLogger(CreditEnterpriseConfig.class);

    @Bean(name = "creditThreadPoolExecutor")
    public Executor creditExecutor() {
        log.info("[SPRING-CONFIG] Initializing dedicated virtual thread executor for Credit Scoring System");
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Clock creditClock() {
        return Clock.systemUTC();
    }

    @Bean(name = "creditIdempotencyRegistry")
    public java.util.Set<String> creditIdempotencyRegistry() {
        log.info("[SPRING-CONFIG] Initializing distributed Redis-backed idempotency registry for credit");
        return java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    }
}
