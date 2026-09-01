package com.fincorex.admin.telemetry;

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
 * Enterprise Micrometer Metrics & Telemetry Collector for Admin & Operations Center (Admin)
 */
@Component
public class AdminTelemetryCollector {

    private static final Logger log = LoggerFactory.getLogger(AdminTelemetryCollector.class);

    private final Counter successCounter;
    private final Counter failureCounter;
    private final Timer executionTimer;

    @Autowired
    public AdminTelemetryCollector(MeterRegistry registry) {
        this.successCounter = Counter.builder("fincorex.admin.operations.success")
                .description("Total successful operations in Admin & Operations Center")
                .tag("module", "admin")
                .register(registry);

        this.failureCounter = Counter.builder("fincorex.admin.operations.failure")
                .description("Total failed operations in Admin & Operations Center")
                .tag("module", "admin")
                .register(registry);

        this.executionTimer = Timer.builder("fincorex.admin.execution.time")
                .description("Latency distribution for Admin & Operations Center execution")
                .tag("module", "admin")
                .register(registry);
    }

    public void recordSuccess(Duration duration) {
        successCounter.increment();
        executionTimer.record(duration);
        log.debug("[TELEMETRY] Recorded successful operation in admin (Duration: {}ms)", duration.toMillis());
    }

    public void recordFailure(String errorCode) {
        failureCounter.increment();
        log.warn("[TELEMETRY-WARN] Recorded failure operation in admin with error code: {}", errorCode);
    }
}
