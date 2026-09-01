package com.fincorex.dispute.controller;

import com.fincorex.dispute.dto.DisputeDTO;
import com.fincorex.dispute.service.DisputeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/dispute")
public class DisputeController {

    private final DisputeService service;

    @Autowired
    public DisputeController(DisputeService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DisputeDTO> createRecord(
            @RequestParam String referenceCode,
            @RequestParam UUID ownerId,
            @RequestParam BigDecimal amount,
            @RequestParam(defaultValue = "ACTIVE") String status) {
        DisputeDTO created = service.createRecord(referenceCode, ownerId, amount, status);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/ref/{referenceCode}")
    public ResponseEntity<DisputeDTO> getByReference(@PathVariable String referenceCode) {
        return ResponseEntity.ok(service.getByReferenceCode(referenceCode));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<DisputeDTO>> getByOwner(@PathVariable UUID ownerId) {
        return ResponseEntity.ok(service.getByOwnerId(ownerId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<DisputeDTO> updateStatus(
            @PathVariable UUID id,
            @RequestParam String status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }
}
