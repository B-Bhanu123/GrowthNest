package com.fincorex.reconciliation.controller;

import com.fincorex.reconciliation.dto.ReconciliationDTO;
import com.fincorex.reconciliation.service.ReconciliationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reconciliation")
public class ReconciliationController {

    private final ReconciliationService service;

    @Autowired
    public ReconciliationController(ReconciliationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ReconciliationDTO> createRecord(
            @RequestParam String referenceCode,
            @RequestParam UUID ownerId,
            @RequestParam BigDecimal amount,
            @RequestParam(defaultValue = "ACTIVE") String status) {
        ReconciliationDTO created = service.createRecord(referenceCode, ownerId, amount, status);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/ref/{referenceCode}")
    public ResponseEntity<ReconciliationDTO> getByReference(@PathVariable String referenceCode) {
        return ResponseEntity.ok(service.getByReferenceCode(referenceCode));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<ReconciliationDTO>> getByOwner(@PathVariable UUID ownerId) {
        return ResponseEntity.ok(service.getByOwnerId(ownerId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ReconciliationDTO> updateStatus(
            @PathVariable UUID id,
            @RequestParam String status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }
}
