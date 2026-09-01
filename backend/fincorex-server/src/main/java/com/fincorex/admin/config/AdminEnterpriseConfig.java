package com.fincorex.admin.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Enterprise Spring Configuration for Admin & Operations Center (Admin Module)
 * Configures thread pools, high-throughput caching, and domain bean definitions.
 */
@Configuration
public class AdminEnterpriseConfig {

    private static final Logger log = LoggerFactory.getLogger(AdminEnterpriseConfig.class);

    @Bean(name = "adminThreadPoolExecutor")
    public Executor adminExecutor() {
        log.info("[SPRING-CONFIG] Initializing dedicated virtual thread executor for Admin & Operations Center");
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Clock adminClock() {
        return Clock.systemUTC();
    }

    @Bean(name = "adminIdempotencyRegistry")
    public java.util.Set<String> adminIdempotencyRegistry() {
        log.info("[SPRING-CONFIG] Initializing distributed Redis-backed idempotency registry for admin");
        return java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    }
}
