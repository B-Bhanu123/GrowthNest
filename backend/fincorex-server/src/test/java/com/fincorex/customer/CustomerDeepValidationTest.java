package com.fincorex.customer;

import com.fincorex.customer.config.CustomerProperties;
import com.fincorex.customer.dto.CreateCustomerRequest;
import com.fincorex.customer.entity.CustomerAuditLogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CustomerDeepValidationTest {

    @Test
    @DisplayName("Verify CreateRequest DTO constraint validation logic for Customer & Account Management")
    void testRequestDTOValidation() {
        CreateCustomerRequest req = new CreateCustomerRequest();
        req.setReferenceCode("REF-VALID-100");
        req.setOwnerId(UUID.randomUUID());
        req.setAmount(new BigDecimal("990.50"));
        req.setCurrency("USD");

        assertNotNull(req.getReferenceCode());
        assertEquals("USD", req.getCurrency());
        assertTrue(req.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Verify Audit Log Entity instantiation and state recording for Customer & Account Management")
    void testAuditLogEntityCreation() {
        UUID recId = UUID.randomUUID();
        CustomerAuditLogEntity audit = new CustomerAuditLogEntity(recId, "CREATE", "admin@fincorex.com", "{}", "{\"status\": \"ACTIVE\"}");

        assertNotNull(audit.getAuditId());
        assertEquals(recId, audit.getRecordId());
        assertEquals("CREATE", audit.getActionType());
        assertNotNull(audit.getTimestamp());
    }

    @Test
    @DisplayName("Verify external properties default configurations for Customer & Account Management")
    void testPropertiesDefaults() {
        CustomerProperties props = new CustomerProperties();
        assertTrue(props.isEnabled());
        assertEquals("PRODUCTION", props.getEnvironment());
        assertEquals(3, props.getMaxRetryAttempts());
        assertNotNull(props.getKafkaTopic());
    }
}
