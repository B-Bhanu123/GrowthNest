package com.fincorex.analytics.telemetry;

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
 * Enterprise Micrometer Metrics & Telemetry Collector for Financial Analytics Engine (Analytics)
 */
@Component
public class AnalyticsTelemetryCollector {

    private static final Logger log = LoggerFactory.getLogger(AnalyticsTelemetryCollector.class);

    private final Counter successCounter;
    private final Counter failureCounter;
    private final Timer executionTimer;

    @Autowired
    public AnalyticsTelemetryCollector(MeterRegistry registry) {
        this.successCounter = Counter.builder("fincorex.analytics.operations.success")
                .description("Total successful operations in Financial Analytics Engine")
                .tag("module", "analytics")
                .register(registry);

        this.failureCounter = Counter.builder("fincorex.analytics.operations.failure")
                .description("Total failed operations in Financial Analytics Engine")
                .tag("module", "analytics")
                .register(registry);

        this.executionTimer = Timer.builder("fincorex.analytics.execution.time")
                .description("Latency distribution for Financial Analytics Engine execution")
                .tag("module", "analytics")
                .register(registry);
    }

    public void recordSuccess(Duration duration) {
        successCounter.increment();
        executionTimer.record(duration);
        log.debug("[TELEMETRY] Recorded successful operation in analytics (Duration: {}ms)", duration.toMillis());
    }

    public void recordFailure(String errorCode) {
        failureCounter.increment();
        log.warn("[TELEMETRY-WARN] Recorded failure operation in analytics with error code: {}", errorCode);
    }
}
