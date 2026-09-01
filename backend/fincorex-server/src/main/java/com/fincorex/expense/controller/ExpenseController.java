package com.fincorex.expense.controller;

import com.fincorex.expense.dto.ExpenseDTO;
import com.fincorex.expense.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/expense")
public class ExpenseController {

    private final ExpenseService service;

    @Autowired
    public ExpenseController(ExpenseService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ExpenseDTO> createRecord(
            @RequestParam String referenceCode,
            @RequestParam UUID ownerId,
            @RequestParam BigDecimal amount,
            @RequestParam(defaultValue = "ACTIVE") String status) {
        ExpenseDTO created = service.createRecord(referenceCode, ownerId, amount, status);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/ref/{referenceCode}")
    public ResponseEntity<ExpenseDTO> getByReference(@PathVariable String referenceCode) {
        return ResponseEntity.ok(service.getByReferenceCode(referenceCode));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<ExpenseDTO>> getByOwner(@PathVariable UUID ownerId) {
        return ResponseEntity.ok(service.getByOwnerId(ownerId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ExpenseDTO> updateStatus(
            @PathVariable UUID id,
            @RequestParam String status) {
        return ResponseEntity.ok(service.updateStatus(id, status));
    }
}
