package com.fincorex.refund.telemetry;

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
 * Enterprise Micrometer Metrics & Telemetry Collector for Refund Management (Refund)
 */
@Component
public class RefundTelemetryCollector {

    private static final Logger log = LoggerFactory.getLogger(RefundTelemetryCollector.class);

    private final Counter successCounter;
    private final Counter failureCounter;
    private final Timer executionTimer;

    @Autowired
    public RefundTelemetryCollector(MeterRegistry registry) {
        this.successCounter = Counter.builder("fincorex.refund.operations.success")
                .description("Total successful operations in Refund Management")
                .tag("module", "refund")
                .register(registry);

        this.failureCounter = Counter.builder("fincorex.refund.operations.failure")
                .description("Total failed operations in Refund Management")
                .tag("module", "refund")
                .register(registry);

        this.executionTimer = Timer.builder("fincorex.refund.execution.time")
                .description("Latency distribution for Refund Management execution")
                .tag("module", "refund")
                .register(registry);
    }

    public void recordSuccess(Duration duration) {
        successCounter.increment();
        executionTimer.record(duration);
        log.debug("[TELEMETRY] Recorded successful operation in refund (Duration: {}ms)", duration.toMillis());
    }

    public void recordFailure(String errorCode) {
        failureCounter.increment();
        log.warn("[TELEMETRY-WARN] Recorded failure operation in refund with error code: {}", errorCode);
    }
}
