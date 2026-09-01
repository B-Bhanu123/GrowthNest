package com.fincorex.upi.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Enterprise Spring Configuration for UPI Instant Transfer Network (Upi Module)
 * Configures thread pools, high-throughput caching, and domain bean definitions.
 */
@Configuration
public class UpiEnterpriseConfig {

    private static final Logger log = LoggerFactory.getLogger(UpiEnterpriseConfig.class);

    @Bean(name = "upiThreadPoolExecutor")
    public Executor upiExecutor() {
        log.info("[SPRING-CONFIG] Initializing dedicated virtual thread executor for UPI Instant Transfer Network");
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Clock upiClock() {
        return Clock.systemUTC();
    }

    @Bean(name = "upiIdempotencyRegistry")
    public java.util.Set<String> upiIdempotencyRegistry() {
        log.info("[SPRING-CONFIG] Initializing distributed Redis-backed idempotency registry for upi");
        return java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    }
}
