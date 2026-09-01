package com.fincorex.lending;

import com.fincorex.lending.entity.LendingSubRecordEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class LendingSubRecordUnitTest {

    @Test
    @DisplayName("Verify sub-record entity fields and defaults for Lending & Underwriting Engine")
    void testSubRecordEntityCreation() {
        UUID parentId = UUID.randomUUID();
        LendingSubRecordEntity sub = new LendingSubRecordEntity(parentId, "AUDIT_DETAILS", new BigDecimal("150.25"), "PROCESSED");

        assertNotNull(sub.getCreatedAt());
        assertEquals(parentId, sub.getParentRecordId());
        assertEquals("AUDIT_DETAILS", sub.getSubType());
        assertEquals(new BigDecimal("150.25"), sub.getSubValue());
        assertEquals("PROCESSED", sub.getStatusFlag());
    }
}
