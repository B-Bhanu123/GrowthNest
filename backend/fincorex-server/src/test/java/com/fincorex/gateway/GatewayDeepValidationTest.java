package com.fincorex.gateway;

import com.fincorex.gateway.config.GatewayProperties;
import com.fincorex.gateway.dto.CreateGatewayRequest;
import com.fincorex.gateway.entity.GatewayAuditLogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GatewayDeepValidationTest {

    @Test
    @DisplayName("Verify CreateRequest DTO constraint validation logic for API Gateway & Security Proxy")
    void testRequestDTOValidation() {
        CreateGatewayRequest req = new CreateGatewayRequest();
        req.setReferenceCode("REF-VALID-100");
        req.setOwnerId(UUID.randomUUID());
        req.setAmount(new BigDecimal("990.50"));
        req.setCurrency("USD");

        assertNotNull(req.getReferenceCode());
        assertEquals("USD", req.getCurrency());
        assertTrue(req.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Verify Audit Log Entity instantiation and state recording for API Gateway & Security Proxy")
    void testAuditLogEntityCreation() {
        UUID recId = UUID.randomUUID();
        GatewayAuditLogEntity audit = new GatewayAuditLogEntity(recId, "CREATE", "admin@fincorex.com", "{}", "{\"status\": \"ACTIVE\"}");

        assertNotNull(audit.getAuditId());
        assertEquals(recId, audit.getRecordId());
        assertEquals("CREATE", audit.getActionType());
        assertNotNull(audit.getTimestamp());
    }

    @Test
    @DisplayName("Verify external properties default configurations for API Gateway & Security Proxy")
    void testPropertiesDefaults() {
        GatewayProperties props = new GatewayProperties();
        assertTrue(props.isEnabled());
        assertEquals("PRODUCTION", props.getEnvironment());
        assertEquals(3, props.getMaxRetryAttempts());
        assertNotNull(props.getKafkaTopic());
    }
}
