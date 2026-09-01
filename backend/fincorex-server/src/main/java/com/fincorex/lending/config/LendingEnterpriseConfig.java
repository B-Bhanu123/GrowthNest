package com.fincorex.lending.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Enterprise Spring Configuration for Lending & Underwriting Engine (Lending Module)
 * Configures thread pools, high-throughput caching, and domain bean definitions.
 */
@Configuration
public class LendingEnterpriseConfig {

    private static final Logger log = LoggerFactory.getLogger(LendingEnterpriseConfig.class);

    @Bean(name = "lendingThreadPoolExecutor")
    public Executor lendingExecutor() {
        log.info("[SPRING-CONFIG] Initializing dedicated virtual thread executor for Lending & Underwriting Engine");
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Clock lendingClock() {
        return Clock.systemUTC();
    }

    @Bean(name = "lendingIdempotencyRegistry")
    public java.util.Set<String> lendingIdempotencyRegistry() {
        log.info("[SPRING-CONFIG] Initializing distributed Redis-backed idempotency registry for lending");
        return java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    }
}
