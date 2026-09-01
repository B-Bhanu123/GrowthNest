package com.fincorex.fraud.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.Duration;

/**
 * Externalized Spring Boot Configuration Properties for Real-Time Fraud Detection Engine (Fraud)
 */
@Configuration
@ConfigurationProperties(prefix = "fincorex.fraud")
public class FraudProperties {

    private boolean enabled = true;
    private String environment = "PRODUCTION";
    private int maxRetryAttempts = 3;
    private Duration timeout = Duration.ofSeconds(5);
    private BigDecimal defaultProcessingFee = new BigDecimal("1.50");
    private String kafkaTopic = "fincorex.events.fraud.v1";
    private CacheConfig cache = new CacheConfig();

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public String getEnvironment() { return environment; }
    public void setEnvironment(String environment) { this.environment = environment; }

    public int getMaxRetryAttempts() { return maxRetryAttempts; }
    public void setMaxRetryAttempts(int maxRetryAttempts) { this.maxRetryAttempts = maxRetryAttempts; }

    public Duration getTimeout() { return timeout; }
    public void setTimeout(Duration timeout) { this.timeout = timeout; }

    public BigDecimal getDefaultProcessingFee() { return defaultProcessingFee; }
    public void setDefaultProcessingFee(BigDecimal defaultProcessingFee) { this.defaultProcessingFee = defaultProcessingFee; }

    public String getKafkaTopic() { return kafkaTopic; }
    public void setKafkaTopic(String kafkaTopic) { this.kafkaTopic = kafkaTopic; }

    public CacheConfig getCache() { return cache; }
    public void setCache(CacheConfig cache) { this.cache = cache; }

    public static class CacheConfig {
        private int timeToLiveMinutes = 60;
        private int maximumSize = 10000;

        public int getTimeToLiveMinutes() { return timeToLiveMinutes; }
        public void setTimeToLiveMinutes(int timeToLiveMinutes) { this.timeToLiveMinutes = timeToLiveMinutes; }

        public int getMaximumSize() { return maximumSize; }
        public void setMaximumSize(int maximumSize) { this.maximumSize = maximumSize; }
    }
}
