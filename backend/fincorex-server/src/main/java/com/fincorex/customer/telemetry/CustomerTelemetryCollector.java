package com.fincorex.customer.telemetry;

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
 * Enterprise Micrometer Metrics & Telemetry Collector for Customer & Account Management (Customer)
 */
@Component
public class CustomerTelemetryCollector {

    private static final Logger log = LoggerFactory.getLogger(CustomerTelemetryCollector.class);

    private final Counter successCounter;
    private final Counter failureCounter;
    private final Timer executionTimer;

    @Autowired
    public CustomerTelemetryCollector(MeterRegistry registry) {
        this.successCounter = Counter.builder("fincorex.customer.operations.success")
                .description("Total successful operations in Customer & Account Management")
                .tag("module", "customer")
                .register(registry);

        this.failureCounter = Counter.builder("fincorex.customer.operations.failure")
                .description("Total failed operations in Customer & Account Management")
                .tag("module", "customer")
                .register(registry);

        this.executionTimer = Timer.builder("fincorex.customer.execution.time")
                .description("Latency distribution for Customer & Account Management execution")
                .tag("module", "customer")
                .register(registry);
    }

    public void recordSuccess(Duration duration) {
        successCounter.increment();
        executionTimer.record(duration);
        log.debug("[TELEMETRY] Recorded successful operation in customer (Duration: {}ms)", duration.toMillis());
    }

    public void recordFailure(String errorCode) {
        failureCounter.increment();
        log.warn("[TELEMETRY-WARN] Recorded failure operation in customer with error code: {}", errorCode);
    }
}
