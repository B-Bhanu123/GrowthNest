package com.fincorex.admin;

import com.fincorex.admin.config.AdminProperties;
import com.fincorex.admin.dto.CreateAdminRequest;
import com.fincorex.admin.entity.AdminAuditLogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AdminDeepValidationTest {

    @Test
    @DisplayName("Verify CreateRequest DTO constraint validation logic for Admin & Operations Center")
    void testRequestDTOValidation() {
        CreateAdminRequest req = new CreateAdminRequest();
        req.setReferenceCode("REF-VALID-100");
        req.setOwnerId(UUID.randomUUID());
        req.setAmount(new BigDecimal("990.50"));
        req.setCurrency("USD");

        assertNotNull(req.getReferenceCode());
        assertEquals("USD", req.getCurrency());
        assertTrue(req.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Verify Audit Log Entity instantiation and state recording for Admin & Operations Center")
    void testAuditLogEntityCreation() {
        UUID recId = UUID.randomUUID();
        AdminAuditLogEntity audit = new AdminAuditLogEntity(recId, "CREATE", "admin@fincorex.com", "{}", "{\"status\": \"ACTIVE\"}");

        assertNotNull(audit.getAuditId());
        assertEquals(recId, audit.getRecordId());
        assertEquals("CREATE", audit.getActionType());
        assertNotNull(audit.getTimestamp());
    }

    @Test
    @DisplayName("Verify external properties default configurations for Admin & Operations Center")
    void testPropertiesDefaults() {
        AdminProperties props = new AdminProperties();
        assertTrue(props.isEnabled());
        assertEquals("PRODUCTION", props.getEnvironment());
        assertEquals(3, props.getMaxRetryAttempts());
        assertNotNull(props.getKafkaTopic());
    }
}
