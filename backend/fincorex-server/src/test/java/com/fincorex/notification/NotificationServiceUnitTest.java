package com.fincorex.notification;

import com.fincorex.notification.dto.NotificationDTO;
import com.fincorex.notification.dto.CreateNotificationRequest;
import com.fincorex.notification.entity.NotificationRecordEntity;
import com.fincorex.notification.repository.NotificationRepository;
import com.fincorex.notification.service.NotificationService;
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
public class NotificationServiceUnitTest {

    @Mock
    private NotificationRepository repository;

    @InjectMocks
    private NotificationService service;

    private NotificationRecordEntity entity;
    private UUID ownerId;

    @BeforeEach
    void setUp() {
        ownerId = UUID.randomUUID();
        entity = new NotificationRecordEntity("REF-10029", ownerId, new BigDecimal("450.00"), "ACTIVE");
        entity.setId(UUID.randomUUID());
    }

    @Test
    void testCreateRecordSuccess() {
        when(repository.save(any(NotificationRecordEntity.class))).thenReturn(entity);

        NotificationDTO result = service.createRecord("REF-10029", ownerId, new BigDecimal("450.00"), "ACTIVE");

        assertNotNull(result);
        assertEquals("REF-10029", result.getReferenceCode());
        assertEquals(new BigDecimal("450.00"), result.getAmount());
        verify(repository, times(1)).save(any());
    }

    @Test
    void testGetByReferenceCodeSuccess() {
        when(repository.findByReferenceCode("REF-10029")).thenReturn(Optional.of(entity));

        NotificationDTO result = service.getByReferenceCode("REF-10029");

        assertNotNull(result);
        assertEquals(ownerId, result.getOwnerId());
    }

    @Test
    void testGetByReferenceCodeNotFoundThrows() {
        when(repository.findByReferenceCode("INVALID")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> service.getByReferenceCode("INVALID"));
    }
}
