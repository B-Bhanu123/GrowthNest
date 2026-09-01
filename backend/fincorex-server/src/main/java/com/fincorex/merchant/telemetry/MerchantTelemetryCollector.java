package com.fincorex.merchant.telemetry;

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
 * Enterprise Micrometer Metrics & Telemetry Collector for Merchant Acquiring Management (Merchant)
 */
@Component
public class MerchantTelemetryCollector {

    private static final Logger log = LoggerFactory.getLogger(MerchantTelemetryCollector.class);

    private final Counter successCounter;
    private final Counter failureCounter;
    private final Timer executionTimer;

    @Autowired
    public MerchantTelemetryCollector(MeterRegistry registry) {
        this.successCounter = Counter.builder("fincorex.merchant.operations.success")
                .description("Total successful operations in Merchant Acquiring Management")
                .tag("module", "merchant")
                .register(registry);

        this.failureCounter = Counter.builder("fincorex.merchant.operations.failure")
                .description("Total failed operations in Merchant Acquiring Management")
                .tag("module", "merchant")
                .register(registry);

        this.executionTimer = Timer.builder("fincorex.merchant.execution.time")
                .description("Latency distribution for Merchant Acquiring Management execution")
                .tag("module", "merchant")
                .register(registry);
    }

    public void recordSuccess(Duration duration) {
        successCounter.increment();
        executionTimer.record(duration);
        log.debug("[TELEMETRY] Recorded successful operation in merchant (Duration: {}ms)", duration.toMillis());
    }

    public void recordFailure(String errorCode) {
        failureCounter.increment();
        log.warn("[TELEMETRY-WARN] Recorded failure operation in merchant with error code: {}", errorCode);
    }
}
