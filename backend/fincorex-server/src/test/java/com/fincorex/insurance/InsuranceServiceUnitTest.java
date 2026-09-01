package com.fincorex.insurance;

import com.fincorex.insurance.dto.InsuranceDTO;
import com.fincorex.insurance.dto.CreateInsuranceRequest;
import com.fincorex.insurance.entity.InsuranceRecordEntity;
import com.fincorex.insurance.repository.InsuranceRepository;
import com.fincorex.insurance.service.InsuranceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InsuranceServiceUnitTest {

    @Mock
    private InsuranceRepository repository;

    @InjectMocks
    private InsuranceService service;

    private InsuranceRecordEntity entity;
    private UUID ownerId;

    @BeforeEach
    void setUp() {
        ownerId = UUID.randomUUID();
        entity = new InsuranceRecordEntity("REF-10029", ownerId, new BigDecimal("450.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());
    }

    @Test
    void testCreateRecordSuccess() {
        when(repository.save(any(InsuranceRecordEntity.class))).thenReturn(entity);

        InsuranceDTO result = service.createRecord("REF-10029", ownerId, new BigDecimal("450.00"), "ACTIVE");

        assertNotNull(result);
        assertEquals("REF-10029", result.getReferenceCode());
        assertEquals(new BigDecimal("450.00"), result.getAmount());
        verify(repository, times(1)).save(any());
    }

    @Test
    void testGetByReferenceCodeSuccess() {
        when(repository.findByReferenceCode("REF-10029")).thenReturn(Optional.of(entity));

        InsuranceDTO result = service.getByReferenceCode("REF-10029");

        assertNotNull(result);
        assertEquals(ownerId, result.getOwnerId());
    }

    @Test
    void testGetByReferenceCodeNotFoundThrows() {
        when(repository.findByReferenceCode("INVALID")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.getByReferenceCode("INVALID"));
    }
}
