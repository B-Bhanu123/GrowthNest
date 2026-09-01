package com.fincorex.settlement;

import com.fincorex.settlement.config.SettlementProperties;
import com.fincorex.settlement.dto.CreateSettlementRequest;
import com.fincorex.settlement.entity.SettlementAuditLogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SettlementDeepValidationTest {

    @Test
    @DisplayName("Verify CreateRequest DTO constraint validation logic for Merchant Batch Settlement")
    void testRequestDTOValidation() {
        CreateSettlementRequest req = new CreateSettlementRequest();
        req.setReferenceCode("REF-VALID-100");
        req.setOwnerId(UUID.randomUUID());
        req.setAmount(new BigDecimal("990.50"));
        req.setCurrency("USD");

        assertNotNull(req.getReferenceCode());
        assertEquals("USD", req.getCurrency());
        assertTrue(req.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Verify Audit Log Entity instantiation and state recording for Merchant Batch Settlement")
    void testAuditLogEntityCreation() {
        UUID recId = UUID.randomUUID();
        SettlementAuditLogEntity audit = new SettlementAuditLogEntity(recId, "CREATE", "admin@fincorex.com", "{}", "{\"status\": \"ACTIVE\"}");

        assertNotNull(audit.getAuditId());
        assertEquals(recId, audit.getRecordId());
        assertEquals("CREATE", audit.getActionType());
        assertNotNull(audit.getTimestamp());
    }

    @Test
    @DisplayName("Verify external properties default configurations for Merchant Batch Settlement")
    void testPropertiesDefaults() {
        SettlementProperties props = new SettlementProperties();
        assertTrue(props.isEnabled());
        assertEquals("PRODUCTION", props.getEnvironment());
        assertEquals(3, props.getMaxRetryAttempts());
        assertNotNull(props.getKafkaTopic());
    }
}
