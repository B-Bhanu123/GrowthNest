package com.fincorex.dispute.telemetry;

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
 * Enterprise Micrometer Metrics & Telemetry Collector for Dispute & Chargeback Handling (Dispute)
 */
@Component
public class DisputeTelemetryCollector {

    private static final Logger log = LoggerFactory.getLogger(DisputeTelemetryCollector.class);

    private final Counter successCounter;
    private final Counter failureCounter;
    private final Timer executionTimer;

    @Autowired
    public DisputeTelemetryCollector(MeterRegistry registry) {
        this.successCounter = Counter.builder("fincorex.dispute.operations.success")
                .description("Total successful operations in Dispute & Chargeback Handling")
                .tag("module", "dispute")
                .register(registry);

        this.failureCounter = Counter.builder("fincorex.dispute.operations.failure")
                .description("Total failed operations in Dispute & Chargeback Handling")
                .tag("module", "dispute")
                .register(registry);

        this.executionTimer = Timer.builder("fincorex.dispute.execution.time")
                .description("Latency distribution for Dispute & Chargeback Handling execution")
                .tag("module", "dispute")
                .register(registry);
    }

    public void recordSuccess(Duration duration) {
        successCounter.increment();
        executionTimer.record(duration);
        log.debug("[TELEMETRY] Recorded successful operation in dispute (Duration: {}ms)", duration.toMillis());
    }

    public void recordFailure(String errorCode) {
        failureCounter.increment();
        log.warn("[TELEMETRY-WARN] Recorded failure operation in dispute with error code: {}", errorCode);
    }
}
