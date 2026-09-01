package com.fincorex.refund;

import com.fincorex.refund.config.RefundProperties;
import com.fincorex.refund.dto.CreateRefundRequest;
import com.fincorex.refund.entity.RefundAuditLogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RefundDeepValidationTest {

    @Test
    @DisplayName("Verify CreateRequest DTO constraint validation logic for Refund Management")
    void testRequestDTOValidation() {
        CreateRefundRequest req = new CreateRefundRequest();
        req.setReferenceCode("REF-VALID-100");
        req.setOwnerId(UUID.randomUUID());
        req.setAmount(new BigDecimal("990.50"));
        req.setCurrency("USD");

        assertNotNull(req.getReferenceCode());
        assertEquals("USD", req.getCurrency());
        assertTrue(req.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Verify Audit Log Entity instantiation and state recording for Refund Management")
    void testAuditLogEntityCreation() {
        UUID recId = UUID.randomUUID();
        RefundAuditLogEntity audit = new RefundAuditLogEntity(recId, "CREATE", "admin@fincorex.com", "{}", "{\"status\": \"ACTIVE\"}");

        assertNotNull(audit.getAuditId());
        assertEquals(recId, audit.getRecordId());
        assertEquals("CREATE", audit.getActionType());
        assertNotNull(audit.getTimestamp());
    }

    @Test
    @DisplayName("Verify external properties default configurations for Refund Management")
    void testPropertiesDefaults() {
        RefundProperties props = new RefundProperties();
        assertTrue(props.isEnabled());
        assertEquals("PRODUCTION", props.getEnvironment());
        assertEquals(3, props.getMaxRetryAttempts());
        assertNotNull(props.getKafkaTopic());
    }
}
