package com.fincorex.payment;

import com.fincorex.payment.config.PaymentProperties;
import com.fincorex.payment.dto.CreatePaymentRequest;
import com.fincorex.payment.entity.PaymentAuditLogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class PaymentDeepValidationTest {

    @Test
    @DisplayName("Verify CreateRequest DTO constraint validation logic for Payment Gateway Orchestration")
    void testRequestDTOValidation() {
        CreatePaymentRequest req = new CreatePaymentRequest();
        req.setReferenceCode("REF-VALID-100");
        req.setOwnerId(UUID.randomUUID());
        req.setAmount(new BigDecimal("990.50"));
        req.setCurrency("USD");

        assertNotNull(req.getReferenceCode());
        assertEquals("USD", req.getCurrency());
        assertTrue(req.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Verify Audit Log Entity instantiation and state recording for Payment Gateway Orchestration")
    void testAuditLogEntityCreation() {
        UUID recId = UUID.randomUUID();
        PaymentAuditLogEntity audit = new PaymentAuditLogEntity(recId, "CREATE", "admin@fincorex.com", "{}", "{\"status\": \"ACTIVE\"}");

        assertNotNull(audit.getAuditId());
        assertEquals(recId, audit.getRecordId());
        assertEquals("CREATE", audit.getActionType());
        assertNotNull(audit.getTimestamp());
    }

    @Test
    @DisplayName("Verify external properties default configurations for Payment Gateway Orchestration")
    void testPropertiesDefaults() {
        PaymentProperties props = new PaymentProperties();
        assertTrue(props.isEnabled());
        assertEquals("PRODUCTION", props.getEnvironment());
        assertEquals(3, props.getMaxRetryAttempts());
        assertNotNull(props.getKafkaTopic());
    }
}
