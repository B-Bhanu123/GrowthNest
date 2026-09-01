package com.fincorex.upi;

import com.fincorex.upi.entity.UpiSubRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UpiSubRecordUnitTest {

    @Test
    @DisplayName("Verify sub-record entity fields and defaults for UPI Instant Transfer Network")
    void testSubRecordEntityCreation() {
        UUID parentId = UUID.randomUUID();
        UpiSubRecordEntity sub = new UpiSubRecordEntity(parentId, "AUDIT_DETAILS", new BigDecimal("150.25"), "PROCESSED");

        assertNotNull(sub.getCreatedAt());
        assertEquals(parentId, sub.getParentRecordId());
        assertEquals("AUDIT_DETAILS", sub.getSubType());
        assertEquals(new BigDecimal("150.25"), sub.getSubValue());
        assertEquals("PROCESSED", sub.getStatusFlag());
    }
}
