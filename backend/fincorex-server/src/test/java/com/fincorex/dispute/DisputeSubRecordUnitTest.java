package com.fincorex.dispute;

import com.fincorex.dispute.entity.DisputeSubRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class DisputeSubRecordUnitTest {

    @Test
    @DisplayName("Verify sub-record entity fields and defaults for Dispute & Chargeback Handling")
    void testSubRecordEntityCreation() {
        UUID parentId = UUID.randomUUID();
        DisputeSubRecordEntity sub = new DisputeSubRecordEntity(parentId, "AUDIT_DETAILS", new BigDecimal("150.25"), "PROCESSED");

        assertNotNull(sub.getCreatedAt());
        assertEquals(parentId, sub.getParentRecordId());
        assertEquals("AUDIT_DETAILS", sub.getSubType());
        assertEquals(new BigDecimal("150.25"), sub.getSubValue());
        assertEquals("PROCESSED", sub.getStatusFlag());
    }
}
