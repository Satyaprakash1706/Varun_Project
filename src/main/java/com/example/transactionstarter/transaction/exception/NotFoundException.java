// Exception thrown when a requested entity cannot be found (mapped to 404).
package com.example.transactionstarter.transaction.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) { super(message); }
}
