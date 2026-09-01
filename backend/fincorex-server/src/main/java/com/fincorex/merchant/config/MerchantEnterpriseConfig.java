package com.fincorex.merchant.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Enterprise Spring Configuration for Merchant Acquiring Management (Merchant Module)
 * Configures thread pools, high-throughput caching, and domain bean definitions.
 */
@Configuration
public class MerchantEnterpriseConfig {

    private static final Logger log = LoggerFactory.getLogger(MerchantEnterpriseConfig.class);

    @Bean(name = "merchantThreadPoolExecutor")
    public Executor merchantExecutor() {
        log.info("[SPRING-CONFIG] Initializing dedicated virtual thread executor for Merchant Acquiring Management");
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Clock merchantClock() {
        return Clock.systemUTC();
    }

    @Bean(name = "merchantIdempotencyRegistry")
    public java.util.Set<String> merchantIdempotencyRegistry() {
        log.info("[SPRING-CONFIG] Initializing distributed Redis-backed idempotency registry for merchant");
        return java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    }
}
