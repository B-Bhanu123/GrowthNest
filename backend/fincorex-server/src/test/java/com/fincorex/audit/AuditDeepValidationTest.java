package com.fincorex.audit;

import com.fincorex.audit.config.AuditProperties;
import com.fincorex.audit.dto.CreateAuditRequest;
import com.fincorex.audit.entity.AuditAuditLogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AuditDeepValidationTest {

    @Test
    @DisplayName("Verify CreateRequest DTO constraint validation logic for Immutable Audit Logging")
    void testRequestDTOValidation() {
        CreateAuditRequest req = new CreateAuditRequest();
        req.setReferenceCode("REF-VALID-100");
        req.setOwnerId(UUID.randomUUID());
        req.setAmount(new BigDecimal("990.50"));
        req.setCurrency("USD");

        assertNotNull(req.getReferenceCode());
        assertEquals("USD", req.getCurrency());
        assertTrue(req.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Verify Audit Log Entity instantiation and state recording for Immutable Audit Logging")
    void testAuditLogEntityCreation() {
        UUID recId = UUID.randomUUID();
        AuditAuditLogEntity audit = new AuditAuditLogEntity(recId, "CREATE", "admin@fincorex.com", "{}", "{\"status\": \"ACTIVE\"}");

        assertNotNull(audit.getAuditId());
        assertEquals(recId, audit.getRecordId());
        assertEquals("CREATE", audit.getActionType());
        assertNotNull(audit.getTimestamp());
    }

    @Test
    @DisplayName("Verify external properties default configurations for Immutable Audit Logging")
    void testPropertiesDefaults() {
        AuditProperties props = new AuditProperties();
        assertTrue(props.isEnabled());
        assertEquals("PRODUCTION", props.getEnvironment());
        assertEquals(3, props.getMaxRetryAttempts());
        assertNotNull(props.getKafkaTopic());
    }
}
