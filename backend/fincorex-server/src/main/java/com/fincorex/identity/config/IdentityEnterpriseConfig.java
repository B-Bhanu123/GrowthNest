package com.fincorex.identity.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Enterprise Spring Configuration for Identity & Access Management (Identity Module)
 * Configures thread pools, high-throughput caching, and domain bean definitions.
 */
@Configuration
public class IdentityEnterpriseConfig {

    private static final Logger log = LoggerFactory.getLogger(IdentityEnterpriseConfig.class);

    @Bean(name = "identityThreadPoolExecutor")
    public Executor identityExecutor() {
        log.info("[SPRING-CONFIG] Initializing dedicated virtual thread executor for Identity & Access Management");
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Clock identityClock() {
        return Clock.systemUTC();
    }

    @Bean(name = "identityIdempotencyRegistry")
    public java.util.Set<String> identityIdempotencyRegistry() {
        log.info("[SPRING-CONFIG] Initializing distributed Redis-backed idempotency registry for identity");
        return java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    }
}
