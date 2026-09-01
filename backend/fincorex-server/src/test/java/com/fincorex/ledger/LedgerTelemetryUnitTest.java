package com.fincorex.ledger;

import com.fincorex.ledger.dto.LedgerSearchCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class LedgerTelemetryUnitTest {

    @Test
    @DisplayName("Verify Double-Entry Financial Ledger SearchCriteria default paging and sorting parameters")
    void testSearchCriteriaDefaults() {
        LedgerSearchCriteria criteria = new LedgerSearchCriteria();
        assertEquals(0, criteria.getPage());
        assertEquals(20, criteria.getSize());
        assertEquals("createdAt", criteria.getSortBy());
        assertEquals("DESC", criteria.getSortDirection());
    }

    @Test
    @DisplayName("Verify Double-Entry Financial Ledger SearchCriteria range filters")
    void testSearchCriteriaFilters() {
        LedgerSearchCriteria criteria = new LedgerSearchCriteria();
        UUID owner = UUID.randomUUID();
        criteria.setOwnerId(owner);
        criteria.setMinAmount(new BigDecimal("100.00"));
        criteria.setMaxAmount(new BigDecimal("5000.00"));
        criteria.setStatuses(List.of("ACTIVE", "COMPLETED"));

        assertEquals(owner, criteria.getOwnerId());
        assertEquals(new BigDecimal("100.00"), criteria.getMinAmount());
        assertEquals(2, criteria.getStatuses().size());
    }
}
