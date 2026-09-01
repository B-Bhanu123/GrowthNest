package com.fincorex.gateway.telemetry;

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
 * Enterprise Micrometer Metrics & Telemetry Collector for API Gateway & Security Proxy (Gateway)
 */
@Component
public class GatewayTelemetryCollector {

    private static final Logger log = LoggerFactory.getLogger(GatewayTelemetryCollector.class);

    private final Counter successCounter;
    private final Counter failureCounter;
    private final Timer executionTimer;

    @Autowired
    public GatewayTelemetryCollector(MeterRegistry registry) {
        this.successCounter = Counter.builder("fincorex.gateway.operations.success")
                .description("Total successful operations in API Gateway & Security Proxy")
                .tag("module", "gateway")
                .register(registry);

        this.failureCounter = Counter.builder("fincorex.gateway.operations.failure")
                .description("Total failed operations in API Gateway & Security Proxy")
                .tag("module", "gateway")
                .register(registry);

        this.executionTimer = Timer.builder("fincorex.gateway.execution.time")
                .description("Latency distribution for API Gateway & Security Proxy execution")
                .tag("module", "gateway")
                .register(registry);
    }

    public void recordSuccess(Duration duration) {
        successCounter.increment();
        executionTimer.record(duration);
        log.debug("[TELEMETRY] Recorded successful operation in gateway (Duration: {}ms)", duration.toMillis());
    }

    public void recordFailure(String errorCode) {
        failureCounter.increment();
        log.warn("[TELEMETRY-WARN] Recorded failure operation in gateway with error code: {}", errorCode);
    }
}
