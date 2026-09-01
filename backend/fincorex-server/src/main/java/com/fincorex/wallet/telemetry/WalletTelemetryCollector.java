package com.fincorex.wallet.telemetry;

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
 * Enterprise Micrometer Metrics & Telemetry Collector for Stored-Value Digital Wallet (Wallet)
 */
@Component
public class WalletTelemetryCollector {

    private static final Logger log = LoggerFactory.getLogger(WalletTelemetryCollector.class);

    private final Counter successCounter;
    private final Counter failureCounter;
    private final Timer executionTimer;

    @Autowired
    public WalletTelemetryCollector(MeterRegistry registry) {
        this.successCounter = Counter.builder("fincorex.wallet.operations.success")
                .description("Total successful operations in Stored-Value Digital Wallet")
                .tag("module", "wallet")
                .register(registry);

        this.failureCounter = Counter.builder("fincorex.wallet.operations.failure")
                .description("Total failed operations in Stored-Value Digital Wallet")
                .tag("module", "wallet")
                .register(registry);

        this.executionTimer = Timer.builder("fincorex.wallet.execution.time")
                .description("Latency distribution for Stored-Value Digital Wallet execution")
                .tag("module", "wallet")
                .register(registry);
    }

    public void recordSuccess(Duration duration) {
        successCounter.increment();
        executionTimer.record(duration);
        log.debug("[TELEMETRY] Recorded successful operation in wallet (Duration: {}ms)", duration.toMillis());
    }

    public void recordFailure(String errorCode) {
        failureCounter.increment();
        log.warn("[TELEMETRY-WARN] Recorded failure operation in wallet with error code: {}", errorCode);
    }
}
