// DTO for status update. Only a TransactionStatus value is required.
package com.example.transactionstarter.transaction.dto;

import com.example.transactionstarter.transaction.TransactionStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateStatusRequest {
    @NotNull
    private TransactionStatus status;

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }
}
