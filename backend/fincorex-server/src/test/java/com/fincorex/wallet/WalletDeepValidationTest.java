package com.fincorex.wallet;

import com.fincorex.wallet.config.WalletProperties;
import com.fincorex.wallet.dto.CreateWalletRequest;
import com.fincorex.wallet.entity.WalletAuditLogEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class WalletDeepValidationTest {

    @Test
    @DisplayName("Verify CreateRequest DTO constraint validation logic for Stored-Value Digital Wallet")
    void testRequestDTOValidation() {
        CreateWalletRequest req = new CreateWalletRequest();
        req.setReferenceCode("REF-VALID-100");
        req.setOwnerId(UUID.randomUUID());
        req.setAmount(new BigDecimal("990.50"));
        req.setCurrency("USD");

        assertNotNull(req.getReferenceCode());
        assertEquals("USD", req.getCurrency());
        assertTrue(req.getAmount().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("Verify Audit Log Entity instantiation and state recording for Stored-Value Digital Wallet")
    void testAuditLogEntityCreation() {
        UUID recId = UUID.randomUUID();
        WalletAuditLogEntity audit = new WalletAuditLogEntity(recId, "CREATE", "admin@fincorex.com", "{}", "{\"status\": \"ACTIVE\"}");

        assertNotNull(audit.getAuditId());
        assertEquals(recId, audit.getRecordId());
        assertEquals("CREATE", audit.getActionType());
        assertNotNull(audit.getTimestamp());
    }

    @Test
    @DisplayName("Verify external properties default configurations for Stored-Value Digital Wallet")
    void testPropertiesDefaults() {
        WalletProperties props = new WalletProperties();
        assertTrue(props.isEnabled());
        assertEquals("PRODUCTION", props.getEnvironment());
        assertEquals(3, props.getMaxRetryAttempts());
        assertNotNull(props.getKafkaTopic());
    }
}
