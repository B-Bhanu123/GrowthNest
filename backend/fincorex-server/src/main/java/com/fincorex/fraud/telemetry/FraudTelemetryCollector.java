package com.fincorex.fraud.telemetry;

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
 * Enterprise Micrometer Metrics & Telemetry Collector for Real-Time Fraud Detection Engine (Fraud)
 */
@Component
public class FraudTelemetryCollector {

    private static final Logger log = LoggerFactory.getLogger(FraudTelemetryCollector.class);

    private final Counter successCounter;
    private final Counter failureCounter;
    private final Timer executionTimer;

    @Autowired
    public FraudTelemetryCollector(MeterRegistry registry) {
        this.successCounter = Counter.builder("fincorex.fraud.operations.success")
                .description("Total successful operations in Real-Time Fraud Detection Engine")
                .tag("module", "fraud")
                .register(registry);

        this.failureCounter = Counter.builder("fincorex.fraud.operations.failure")
                .description("Total failed operations in Real-Time Fraud Detection Engine")
                .tag("module", "fraud")
                .register(registry);

        this.executionTimer = Timer.builder("fincorex.fraud.execution.time")
                .description("Latency distribution for Real-Time Fraud Detection Engine execution")
                .tag("module", "fraud")
                .register(registry);
    }

    public void recordSuccess(Duration duration) {
        successCounter.increment();
        executionTimer.record(duration);
        log.debug("[TELEMETRY] Recorded successful operation in fraud (Duration: {}ms)", duration.toMillis());
    }

    public void recordFailure(String errorCode) {
        failureCounter.increment();
        log.warn("[TELEMETRY-WARN] Recorded failure operation in fraud with error code: {}", errorCode);
    }
}
