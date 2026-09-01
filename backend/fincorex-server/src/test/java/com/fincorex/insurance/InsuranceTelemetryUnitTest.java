package com.fincorex.insurance;

import com.fincorex.insurance.dto.InsuranceSearchCriteria;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class InsuranceTelemetryUnitTest {

    @Test
    @DisplayName("Verify Insurance Policy System SearchCriteria default paging and sorting parameters")
    void testSearchCriteriaDefaults() {
        InsuranceSearchCriteria criteria = new InsuranceSearchCriteria();
        assertEquals(0, criteria.getPage());
        assertEquals(20, criteria.getSize());
        assertEquals("createdAt", criteria.getSortBy());
        assertEquals("DESC", criteria.getSortDirection());
    }

    @Test
    @DisplayName("Verify Insurance Policy System SearchCriteria range filters")
    void testSearchCriteriaFilters() {
        InsuranceSearchCriteria criteria = new InsuranceSearchCriteria();
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
