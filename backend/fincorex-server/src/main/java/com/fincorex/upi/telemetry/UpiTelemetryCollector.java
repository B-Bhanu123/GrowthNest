package com.fincorex.upi.telemetry;

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
 * Enterprise Micrometer Metrics & Telemetry Collector for UPI Instant Transfer Network (Upi)
 */
@Component
public class UpiTelemetryCollector {

    private static final Logger log = LoggerFactory.getLogger(UpiTelemetryCollector.class);

    private final Counter successCounter;
    private final Counter failureCounter;
    private final Timer executionTimer;

    @Autowired
    public UpiTelemetryCollector(MeterRegistry registry) {
        this.successCounter = Counter.builder("fincorex.upi.operations.success")
                .description("Total successful operations in UPI Instant Transfer Network")
                .tag("module", "upi")
                .register(registry);

        this.failureCounter = Counter.builder("fincorex.upi.operations.failure")
                .description("Total failed operations in UPI Instant Transfer Network")
                .tag("module", "upi")
                .register(registry);

        this.executionTimer = Timer.builder("fincorex.upi.execution.time")
                .description("Latency distribution for UPI Instant Transfer Network execution")
                .tag("module", "upi")
                .register(registry);
    }

    public void recordSuccess(Duration duration) {
        successCounter.increment();
        executionTimer.record(duration);
        log.debug("[TELEMETRY] Recorded successful operation in upi (Duration: {}ms)", duration.toMillis());
    }

    public void recordFailure(String errorCode) {
        failureCounter.increment();
        log.warn("[TELEMETRY-WARN] Recorded failure operation in upi with error code: {}", errorCode);
    }
}
