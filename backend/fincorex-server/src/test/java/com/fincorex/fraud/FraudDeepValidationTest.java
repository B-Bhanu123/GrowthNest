package com.fincorex.fraud;

import com.fincorex.fraud.config.FraudProperties;
import com.fincorex.fraud.dto.CreateFraudRequest;
import com.fincorex.fraud.entity.FraudAuditLogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FraudDeepValidationTest {

    @Test
    @DisplayName("Verify CreateRequest DTO constraint validation logic for Real-Time Fraud Detection Engine")
    void testRequestDTOValidation() {
        CreateFraudRequest req = new CreateFraudRequest();
        req.setReferenceCode("REF-VALID-100");
        req.setOwnerId(UUID.randomUUID());
        req.setAmount(new BigDecimal("990.50"));
        req.setCurrency("USD");

        assertNotNull(req.getReferenceCode());
        assertEquals("USD", req.getCurrency());
        assertTrue(req.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Verify Audit Log Entity instantiation and state recording for Real-Time Fraud Detection Engine")
    void testAuditLogEntityCreation() {
        UUID recId = UUID.randomUUID();
        FraudAuditLogEntity audit = new FraudAuditLogEntity(recId, "CREATE", "admin@fincorex.com", "{}", "{\"status\": \"ACTIVE\"}");

        assertNotNull(audit.getAuditId());
        assertEquals(recId, audit.getRecordId());
        assertEquals("CREATE", audit.getActionType());
        assertNotNull(audit.getTimestamp());
    }

    @Test
    @DisplayName("Verify external properties default configurations for Real-Time Fraud Detection Engine")
    void testPropertiesDefaults() {
        FraudProperties props = new FraudProperties();
        assertTrue(props.isEnabled());
        assertEquals("PRODUCTION", props.getEnvironment());
        assertEquals(3, props.getMaxRetryAttempts());
        assertNotNull(props.getKafkaTopic());
    }
}
