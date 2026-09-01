package com.fincorex.accounting.controller;

import com.fincorex.accounting.dto.AccountingDTO;
import com.fincorex.accounting.service.AccountingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounting")
public class AccountingController {

    private final AccountingService service;

    @Autowired
    public AccountingController(AccountingService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AccountingDTO> createRecord(
            @RequestParam String referenceCode,
            @RequestParam UUID ownerId,
            @RequestParam BigDecimal amount,
            @RequestParam(defaultValue = "ACTIVE") String status) {
        AccountingDTO created = service.createRecord(referenceCode, ownerId, amount, status);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/ref/{referenceCode}")
    public ResponseEntity<AccountingDTO> getByReference(@PathVariable String referenceCode) {
        return ResponseEntity.ok(service.getByReferenceCode(referenceCode));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<AccountingDTO>> getByOwner(@PathVariable UUID ownerId) {
        return ResponseEntity.ok(service.getByOwnerId(ownerId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AccountingDTO> updateStatus(
            @PathVariable UUID id,
            @RequestParam String status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }
}
