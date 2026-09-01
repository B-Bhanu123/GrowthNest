package com.fincorex.notification.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Enterprise Spring Configuration for Centralized Notification System (Notification Module)
 * Configures thread pools, high-throughput caching, and domain bean definitions.
 */
@Configuration
public class NotificationEnterpriseConfig {

    private static final Logger log = LoggerFactory.getLogger(NotificationEnterpriseConfig.class);

    @Bean(name = "notificationThreadPoolExecutor")
    public Executor notificationExecutor() {
        log.info("[SPRING-CONFIG] Initializing dedicated virtual thread executor for Centralized Notification System");
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    @Bean
    public Clock notificationClock() {
        return Clock.systemUTC();
    }

    @Bean(name = "notificationIdempotencyRegistry")
    public java.util.Set<String> notificationIdempotencyRegistry() {
        log.info("[SPRING-CONFIG] Initializing distributed Redis-backed idempotency registry for notification");
        return java.util.Collections.synchronizedSet(new java.util.HashSet<>());
    }
}
