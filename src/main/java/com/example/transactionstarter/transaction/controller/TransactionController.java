package com.example.transactionstarter.transaction.controller;

import com.example.transactionstarter.transaction.dto.CreateTransactionRequest;
import com.example.transactionstarter.transaction.dto.TransactionResponse;
import com.example.transactionstarter.transaction.dto.UpdateStatusRequest;
import com.example.transactionstarter.transaction.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping("/transactions")
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody CreateTransactionRequest req) {
        TransactionResponse r = service.createTransaction(req);
        return ResponseEntity.ok(r);
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<TransactionResponse> get(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(service.getTransaction(id));
    }

    @PatchMapping("/transactions/{id}/status")
    public ResponseEntity<TransactionResponse> updateStatus(@PathVariable("id") UUID id, @Valid @RequestBody UpdateStatusRequest req) {
        return ResponseEntity.ok(service.updateStatus(id, req));
    }

    @GetMapping("/customers/{customerId}/transactions")
    public ResponseEntity<List<TransactionResponse>> getCustomerTx(@PathVariable String customerId) {
        return ResponseEntity.ok(service.getCustomerTransactions(customerId));
    }
}
