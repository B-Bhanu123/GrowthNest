package com.fincorex.lending.telemetry;

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
 * Enterprise Micrometer Metrics & Telemetry Collector for Lending & Underwriting Engine (Lending)
 */
@Component
public class LendingTelemetryCollector {

    private static final Logger log = LoggerFactory.getLogger(LendingTelemetryCollector.class);

    private final Counter successCounter;
    private final Counter failureCounter;
    private final Timer executionTimer;

    @Autowired
    public LendingTelemetryCollector(MeterRegistry registry) {
        this.successCounter = Counter.builder("fincorex.lending.operations.success")
                .description("Total successful operations in Lending & Underwriting Engine")
                .tag("module", "lending")
                .register(registry);

        this.failureCounter = Counter.builder("fincorex.lending.operations.failure")
                .description("Total failed operations in Lending & Underwriting Engine")
                .tag("module", "lending")
                .register(registry);

        this.executionTimer = Timer.builder("fincorex.lending.execution.time")
                .description("Latency distribution for Lending & Underwriting Engine execution")
                .tag("module", "lending")
                .register(registry);
    }

    public void recordSuccess(Duration duration) {
        successCounter.increment();
        executionTimer.record(duration);
        log.debug("[TELEMETRY] Recorded successful operation in lending (Duration: {}ms)", duration.toMillis());
    }

    public void recordFailure(String errorCode) {
        failureCounter.increment();
        log.warn("[TELEMETRY-WARN] Recorded failure operation in lending with error code: {}", errorCode);
    }
}
