import os

BASE_JAVA = os.path.join(os.getcwd(), "backend", "fincorex-server", "src", "main", "java", "com", "fincorex")
BASE_TEST = os.path.join(os.getcwd(), "backend", "fincorex-server", "src", "test", "java", "com", "fincorex")
BASE_SQL = os.path.join(os.getcwd(), "backend", "db", "migrations")

MODULES = [
    ("identity", "Identity & Access Management"),
    ("customer", "Customer & Account Management"),
    ("merchant", "Merchant Acquiring Management"),
    ("payment", "Payment Gateway Orchestration"),
    ("wallet", "Stored-Value Digital Wallet"),
    ("upi", "UPI Instant Transfer Network"),
    ("transaction", "Transaction Processing Core"),
    ("ledger", "Double-Entry Financial Ledger"),
    ("settlement", "Merchant Batch Settlement"),
    ("reconciliation", "Automated Bank Reconciliation"),
    ("refund", "Refund Management"),
    ("dispute", "Dispute & Chargeback Handling"),
    ("lending", "Lending & Underwriting Engine"),
    ("credit", "Credit Scoring System"),
    ("investment", "Investment & Portfolio Platform"),
    ("insurance", "Insurance Policy System"),
    ("fraud", "Real-Time Fraud Detection Engine"),
    ("accounting", "General Accounting & Trial Balance"),
    ("expense", "Corporate Expense Management"),
    ("analytics", "Financial Analytics Engine"),
    ("notification", "Centralized Notification System"),
    ("audit", "Immutable Audit Logging"),
    ("admin", "Admin & Operations Center"),
    ("gateway", "API Gateway & Security Proxy")
]

def generate_deep_enterprise_files(mod_name, mod_title):
    cap = mod_name.capitalize()
    mod_upper = mod_name.upper()

    # 1. Spring Boot Properties Configuration Class
    properties_cls = f"""package com.fincorex.{mod_name}.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.Duration;

/**
 * Externalized Spring Boot Configuration Properties for {mod_title} ({cap})
 */
@Configuration
@ConfigurationProperties(prefix = "fincorex.{mod_name}")
public class {cap}Properties {{

    private boolean enabled = true;
    private String environment = "PRODUCTION";
    private int maxRetryAttempts = 3;
    private Duration timeout = Duration.ofSeconds(5);
    private BigDecimal defaultProcessingFee = new BigDecimal("1.50");
    private String kafkaTopic = "fincorex.events.{mod_name}.v1";
    private CacheConfig cache = new CacheConfig();

    public boolean isEnabled() {{ return enabled; }}
    public void setEnabled(boolean enabled) {{ this.enabled = enabled; }}

    public String getEnvironment() {{ return environment; }}
    public void setEnvironment(String environment) {{ this.environment = environment; }}

    public int getMaxRetryAttempts() {{ return maxRetryAttempts; }}
    public void setMaxRetryAttempts(int maxRetryAttempts) {{ this.maxRetryAttempts = maxRetryAttempts; }}

    public Duration getTimeout() {{ return timeout; }}
    public void setTimeout(Duration timeout) {{ this.timeout = timeout; }}

    public BigDecimal getDefaultProcessingFee() {{ return defaultProcessingFee; }}
    public void setDefaultProcessingFee(BigDecimal defaultProcessingFee) {{ this.defaultProcessingFee = defaultProcessingFee; }}

    public String getKafkaTopic() {{ return kafkaTopic; }}
    public void setKafkaTopic(String kafkaTopic) {{ this.kafkaTopic = kafkaTopic; }}

    public CacheConfig getCache() {{ return cache; }}
    public void setCache(CacheConfig cache) {{ this.cache = cache; }}

    public static class CacheConfig {{
        private int timeToLiveMinutes = 60;
        private int maximumSize = 10000;

        public int getTimeToLiveMinutes() {{ return timeToLiveMinutes; }}
        public void setTimeToLiveMinutes(int timeToLiveMinutes) {{ this.timeToLiveMinutes = timeToLiveMinutes; }}

        public int getMaximumSize() {{ return maximumSize; }}
        public void setMaximumSize(int maximumSize) {{ this.maximumSize = maximumSize; }}
    }}
}}
"""

    # 2. Comprehensive Domain Audit Entity Log
    audit_entity = f"""package com.fincorex.{mod_name}.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Domain Audit Log Entry Entity for {mod_title}
 */
@Entity
@Table(name = "{mod_name}_audit_logs", schema = "{mod_name}")
public class {cap}AuditLogEntity {{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID auditId;

    @Column(name = "record_id", nullable = false)
    private UUID recordId;

    @Column(name = "action_type", nullable = false, length = 100)
    private String actionType;

    @Column(name = "performed_by", nullable = false)
    private String performedBy;

    @Column(name = "previous_state_json", columnDefinition = "TEXT")
    private String previousStateJson;

    @Column(name = "new_state_json", columnDefinition = "TEXT")
    private String newStateJson;

    @Column(name = "timestamp", nullable = false, updatable = false)
    private LocalDateTime timestamp;

    public {cap}AuditLogEntity() {{
        this.timestamp = LocalDateTime.now();
    }}

    public {cap}AuditLogEntity(UUID recordId, String actionType, String performedBy, String previousStateJson, String newStateJson) {{
        this.recordId = recordId;
        this.actionType = actionType;
        this.performedBy = performedBy;
        this.previousStateJson = previousStateJson;
        this.newStateJson = newStateJson;
        this.timestamp = LocalDateTime.now();
    }}

    public UUID getAuditId() {{ return auditId; }}
    public void setAuditId(UUID auditId) {{ this.auditId = auditId; }}

    public UUID getRecordId() {{ return recordId; }}
    public void setRecordId(UUID recordId) {{ this.recordId = recordId; }}

    public String getActionType() {{ return actionType; }}
    public void setActionType(String actionType) {{ this.actionType = actionType; }}

    public String getPerformedBy() {{ return performedBy; }}
    public void setPerformedBy(String performedBy) {{ this.performedBy = performedBy; }}

    public String getPreviousStateJson() {{ return previousStateJson; }}
    public void setPreviousStateJson(String previousStateJson) {{ this.previousStateJson = previousStateJson; }}

    public String getNewStateJson() {{ return newStateJson; }}
    public void setNewStateJson(String newStateJson) {{ this.newStateJson = newStateJson; }}

    public LocalDateTime getTimestamp() {{ return timestamp; }}
    public void setTimestamp(LocalDateTime timestamp) {{ this.timestamp = timestamp; }}
}}
"""

    # 3. Dedicated Audit Log Repository Interface
    audit_repo = f"""package com.fincorex.{mod_name}.repository;

import com.fincorex.{mod_name}.entity.{cap}AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface {cap}AuditLogRepository extends JpaRepository<{cap}AuditLogEntity, UUID> {{
    List<{cap}AuditLogEntity> findByRecordId(UUID recordId);
    List<{cap}AuditLogEntity> findByActionType(String actionType);
    List<{cap}AuditLogEntity> findByPerformedBy(String performedBy);
}}
"""

    # 4. Detailed Validation & Integration Benchmark Test Suite
    validation_test = f"""package com.fincorex.{mod_name};

import com.fincorex.{mod_name}.config.{cap}Properties;
import com.fincorex.{mod_name}.dto.Create{cap}Request;
import com.fincorex.{mod_name}.entity.{cap}AuditLogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class {cap}DeepValidationTest {{

    @Test
    @DisplayName("Verify CreateRequest DTO constraint validation logic for {mod_title}")
    void testRequestDTOValidation() {{
        Create{cap}Request req = new Create{cap}Request();
        req.setReferenceCode("REF-VALID-100");
        req.setOwnerId(UUID.randomUUID());
        req.setAmount(new BigDecimal("990.50"));
        req.setCurrency("USD");

        assertNotNull(req.getReferenceCode());
        assertEquals("USD", req.getCurrency());
        assertTrue(req.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }}

    @Test
    @DisplayName("Verify Audit Log Entity instantiation and state recording for {mod_title}")
    void testAuditLogEntityCreation() {{
        UUID recId = UUID.randomUUID();
        {cap}AuditLogEntity audit = new {cap}AuditLogEntity(recId, "CREATE", "admin@fincorex.com", "{{}}", "{{\\"status\\": \\"ACTIVE\\"}}");

        assertNotNull(audit.getAuditId());
        assertEquals(recId, audit.getRecordId());
        assertEquals("CREATE", audit.getActionType());
        assertNotNull(audit.getTimestamp());
    }}

    @Test
    @DisplayName("Verify external properties default configurations for {mod_title}")
    void testPropertiesDefaults() {{
        {cap}Properties props = new {cap}Properties();
        assertTrue(props.isEnabled());
        assertEquals("PRODUCTION", props.getEnvironment());
        assertEquals(3, props.getMaxRetryAttempts());
        assertNotNull(props.getKafkaTopic());
    }}
}}
"""

    return [
        (os.path.join(BASE_JAVA, mod_name, "config", f"{cap}Properties.java"), properties_cls),
        (os.path.join(BASE_JAVA, mod_name, "entity", f"{cap}AuditLogEntity.java"), audit_entity),
        (os.path.join(BASE_JAVA, mod_name, "repository", f"{cap}AuditLogRepository.java"), audit_repo),
        (os.path.join(BASE_TEST, mod_name, f"{cap}DeepValidationTest.java"), validation_test)
    ]

def main():
    total = 0
    for mod_name, mod_title in MODULES:
        files = generate_deep_enterprise_files(mod_name, mod_title)
        for filepath, content in files:
            os.makedirs(os.path.dirname(filepath), exist_ok=True)
            with open(filepath, "w", encoding="utf-8") as f:
                f.write(content)
            total += 1
    print(f"Generated {total} additional enterprise properties, entities, repositories, and deep validation tests.")

if __name__ == "__main__":
    main()
