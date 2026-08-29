package com.example.transactionstarter.transaction.service;

import com.example.transactionstarter.transaction.Transaction;
import com.example.transactionstarter.transaction.TransactionStatus;
import com.example.transactionstarter.transaction.TransactionRepository;
import com.example.transactionstarter.transaction.dto.CreateTransactionRequest;
import com.example.transactionstarter.transaction.dto.TransactionResponse;
import com.example.transactionstarter.transaction.dto.UpdateStatusRequest;
import com.example.transactionstarter.transaction.exception.BadRequestException;
import com.example.transactionstarter.transaction.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public TransactionResponse createTransaction(CreateTransactionRequest req) {
        // business validations beyond annotations
        if (req.getCurrency() == null || req.getCurrency().length() != 3) {
            throw new BadRequestException("currency must be a 3-letter ISO code");
        }

        Transaction tx = new Transaction();
        tx.setId(UUID.randomUUID());
        tx.setCustomerId(req.getCustomerId());
        tx.setAmount(req.getAmount());
        tx.setCurrency(req.getCurrency().toUpperCase());
        tx.setType(req.getType());
        tx.setStatus(TransactionStatus.PENDING); // initial status forced to PENDING

        repository.save(tx);

        return toResponse(tx);
    }

    @Transactional(readOnly = true)
    public TransactionResponse getTransaction(UUID id) {
        Transaction tx = repository.findById(id).orElseThrow(() -> new NotFoundException("Transaction not found"));
        return toResponse(tx);
    }

    @Transactional
    public TransactionResponse updateStatus(UUID id, UpdateStatusRequest req) {
        Transaction tx = repository.findById(id).orElseThrow(() -> new NotFoundException("Transaction not found"));

        TransactionStatus current = tx.getStatus();
        TransactionStatus next = req.getStatus();

        // Only allow transitions from PENDING -> (COMPLETED|FAILED)
        if (current != TransactionStatus.PENDING) {
            throw new BadRequestException("Can only change status from PENDING");
        }
        if (next == TransactionStatus.PENDING) {
            throw new BadRequestException("Cannot transition back to PENDING");
        }

        tx.setStatus(next);
        repository.save(tx);
        return toResponse(tx);
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> getCustomerTransactions(String customerId) {
        return repository.findByCustomerId(customerId).stream().map(this::toResponse).collect(Collectors.toList());
    }

    private TransactionResponse toResponse(Transaction tx) {
        TransactionResponse r = new TransactionResponse();
        r.setId(tx.getId());
        r.setCustomerId(tx.getCustomerId());
        r.setAmount(tx.getAmount());
        r.setCurrency(tx.getCurrency());
        r.setType(tx.getType());
        r.setStatus(tx.getStatus());
        return r;
    }
}
