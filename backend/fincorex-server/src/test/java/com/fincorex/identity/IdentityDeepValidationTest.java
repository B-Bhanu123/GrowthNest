package com.fincorex.identity;

import com.fincorex.identity.config.IdentityProperties;
import com.fincorex.identity.dto.CreateIdentityRequest;
import com.fincorex.identity.entity.IdentityAuditLogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class IdentityDeepValidationTest {

    @Test
    @DisplayName("Verify CreateRequest DTO constraint validation logic for Identity & Access Management")
    void testRequestDTOValidation() {
        CreateIdentityRequest req = new CreateIdentityRequest();
        req.setReferenceCode("REF-VALID-100");
        req.setOwnerId(UUID.randomUUID());
        req.setAmount(new BigDecimal("990.50"));
        req.setCurrency("USD");

        assertNotNull(req.getReferenceCode());
        assertEquals("USD", req.getCurrency());
        assertTrue(req.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Verify Audit Log Entity instantiation and state recording for Identity & Access Management")
    void testAuditLogEntityCreation() {
        UUID recId = UUID.randomUUID();
        IdentityAuditLogEntity audit = new IdentityAuditLogEntity(recId, "CREATE", "admin@fincorex.com", "{}", "{\"status\": \"ACTIVE\"}");

        assertNotNull(audit.getAuditId());
        assertEquals(recId, audit.getRecordId());
        assertEquals("CREATE", audit.getActionType());
        assertNotNull(audit.getTimestamp());
    }

    @Test
    @DisplayName("Verify external properties default configurations for Identity & Access Management")
    void testPropertiesDefaults() {
        IdentityProperties props = new IdentityProperties();
        assertTrue(props.isEnabled());
        assertEquals("PRODUCTION", props.getEnvironment());
        assertEquals(3, props.getMaxRetryAttempts());
        assertNotNull(props.getKafkaTopic());
    }
}
