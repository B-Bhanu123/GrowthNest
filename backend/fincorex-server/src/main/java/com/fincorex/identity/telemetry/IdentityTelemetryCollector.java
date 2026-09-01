package com.fincorex.identity.telemetry;

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
 * Enterprise Micrometer Metrics & Telemetry Collector for Identity & Access Management (Identity)
 */
@Component
public class IdentityTelemetryCollector {

    private static final Logger log = LoggerFactory.getLogger(IdentityTelemetryCollector.class);

    private final Counter successCounter;
    private final Counter failureCounter;
    private final Timer executionTimer;

    @Autowired
    public IdentityTelemetryCollector(MeterRegistry registry) {
        this.successCounter = Counter.builder("fincorex.identity.operations.success")
                .description("Total successful operations in Identity & Access Management")
                .tag("module", "identity")
                .register(registry);

        this.failureCounter = Counter.builder("fincorex.identity.operations.failure")
                .description("Total failed operations in Identity & Access Management")
                .tag("module", "identity")
                .register(registry);

        this.executionTimer = Timer.builder("fincorex.identity.execution.time")
                .description("Latency distribution for Identity & Access Management execution")
                .tag("module", "identity")
                .register(registry);
    }

    public void recordSuccess(Duration duration) {
        successCounter.increment();
        executionTimer.record(duration);
        log.debug("[TELEMETRY] Recorded successful operation in identity (Duration: {}ms)", duration.toMillis());
    }

    public void recordFailure(String errorCode) {
        failureCounter.increment();
        log.warn("[TELEMETRY-WARN] Recorded failure operation in identity with error code: {}", errorCode);
    }
}
