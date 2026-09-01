package com.fincorex.notification.controller;

import com.fincorex.notification.dto.NotificationDTO;
import com.fincorex.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notification")
public class NotificationController {

    private final NotificationService service;

    @Autowired
    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<NotificationDTO> createRecord(
            @RequestParam String referenceCode,
            @RequestParam UUID ownerId,
            @RequestParam BigDecimal amount,
            @RequestParam(defaultValue = "ACTIVE") String status) {
        NotificationDTO created = service.createRecord(referenceCode, ownerId, amount, status);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/ref/{referenceCode}")
    public ResponseEntity<NotificationDTO> getByReference(@PathVariable String referenceCode) {
        return ResponseEntity.ok(service.getByReferenceCode(referenceCode));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<NotificationDTO>> getByOwner(@PathVariable UUID ownerId) {
        return ResponseEntity.ok(service.getByOwnerId(ownerId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<NotificationDTO> updateStatus(
            @PathVariable UUID id,
            @RequestParam String status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }
}
