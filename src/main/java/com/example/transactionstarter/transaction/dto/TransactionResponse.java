package com.example.transactionstarter.transaction.dto;

import com.example.transactionstarter.transaction.TransactionStatus;
import com.example.transactionstarter.transaction.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionResponse {
    private UUID id;
    private String customerId;
    private BigDecimal amount;
    private String currency;
    private TransactionType type;
    private TransactionStatus status;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}
