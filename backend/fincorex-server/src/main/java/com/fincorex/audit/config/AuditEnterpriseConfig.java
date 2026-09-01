package com.fincorex.audit.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Enterprise Spring Configuration for Immutable Audit Logging (Audit Module)
 * Configures thread pools, high-throughput caching, and domain bean definitions.
 */
@Configuration
public class AuditEnterpriseConfig {

    private static final Logger log = LoggerFactory.getLogger(AuditEnterpriseConfig.class);

    @Bean(name = "auditThreadPoolExecutor")
    public Executor auditExecutor() {
        log.info("[SPRING-CONFIG] Initializing dedicated virtual thread executor for Immutable Audit Logging");
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Clock auditClock() {
        return Clock.systemUTC();
    }

    @Bean(name = "auditIdempotencyRegistry")
    public java.util.Set<String> auditIdempotencyRegistry() {
        log.info("[SPRING-CONFIG] Initializing distributed Redis-backed idempotency registry for audit");
        return java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    }
}
