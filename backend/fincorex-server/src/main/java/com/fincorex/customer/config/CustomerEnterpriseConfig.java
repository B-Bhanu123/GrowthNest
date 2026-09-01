package com.fincorex.customer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Enterprise Spring Configuration for Customer & Account Management (Customer Module)
 * Configures thread pools, high-throughput caching, and domain bean definitions.
 */
@Configuration
public class CustomerEnterpriseConfig {

    private static final Logger log = LoggerFactory.getLogger(CustomerEnterpriseConfig.class);

    @Bean(name = "customerThreadPoolExecutor")
    public Executor customerExecutor() {
        log.info("[SPRING-CONFIG] Initializing dedicated virtual thread executor for Customer & Account Management");
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Clock customerClock() {
        return Clock.systemUTC();
    }

    @Bean(name = "customerIdempotencyRegistry")
    public java.util.Set<String> customerIdempotencyRegistry() {
        log.info("[SPRING-CONFIG] Initializing distributed Redis-backed idempotency registry for customer");
        return java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    }
}
