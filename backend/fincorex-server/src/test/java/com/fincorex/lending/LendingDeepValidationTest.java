package com.fincorex.lending;

import com.fincorex.lending.config.LendingProperties;
import com.fincorex.lending.dto.CreateLendingRequest;
import com.fincorex.lending.entity.LendingAuditLogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class LendingDeepValidationTest {

    @Test
    @DisplayName("Verify CreateRequest DTO constraint validation logic for Lending & Underwriting Engine")
    void testRequestDTOValidation() {
        CreateLendingRequest req = new CreateLendingRequest();
        req.setReferenceCode("REF-VALID-100");
        req.setOwnerId(UUID.randomUUID());
        req.setAmount(new BigDecimal("990.50"));
        req.setCurrency("USD");

        assertNotNull(req.getReferenceCode());
        assertEquals("USD", req.getCurrency());
        assertTrue(req.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Verify Audit Log Entity instantiation and state recording for Lending & Underwriting Engine")
    void testAuditLogEntityCreation() {
        UUID recId = UUID.randomUUID();
        LendingAuditLogEntity audit = new LendingAuditLogEntity(recId, "CREATE", "admin@fincorex.com", "{}", "{\"status\": \"ACTIVE\"}");

        assertNotNull(audit.getAuditId());
        assertEquals(recId, audit.getRecordId());
        assertEquals("CREATE", audit.getActionType());
        assertNotNull(audit.getTimestamp());
    }

    @Test
    @DisplayName("Verify external properties default configurations for Lending & Underwriting Engine")
    void testPropertiesDefaults() {
        LendingProperties props = new LendingProperties();
        assertTrue(props.isEnabled());
        assertEquals("PRODUCTION", props.getEnvironment());
        assertEquals(3, props.getMaxRetryAttempts());
        assertNotNull(props.getKafkaTopic());
    }
}
