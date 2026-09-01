package com.fincorex.notification.telemetry;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Enterprise Micrometer Metrics & Telemetry Collector for Centralized Notification System (Notification)
 */
@Component
public class NotificationTelemetryCollector {

    private static final Logger log = LoggerFactory.getLogger(NotificationTelemetryCollector.class);

    private final Counter successCounter;
    private final Counter failureCounter;
    private final Timer executionTimer;

    @Autowired
    public NotificationTelemetryCollector(MeterRegistry registry) {
        this.successCounter = Counter.builder("fincorex.notification.operations.success")
                .description("Total successful operations in Centralized Notification System")
                .tag("module", "notification")
                .register(registry);

        this.failureCounter = Counter.builder("fincorex.notification.operations.failure")
                .description("Total failed operations in Centralized Notification System")
                .tag("module", "notification")
                .register(registry);

        this.executionTimer = Timer.builder("fincorex.notification.execution.time")
                .description("Latency distribution for Centralized Notification System execution")
                .tag("module", "notification")
                .register(registry);
    }

    public void recordSuccess(Duration duration) {
        successCounter.increment();
        executionTimer.record(duration);
        log.debug("[TELEMETRY] Recorded successful operation in notification (Duration: {}ms)", duration.toMillis());
    }

    public void recordFailure(String errorCode) {
        failureCounter.increment();
        log.warn("[TELEMETRY-WARN] Recorded failure operation in notification with error code: {}", errorCode);
    }
}
