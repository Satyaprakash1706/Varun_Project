// Exception thrown for 400 Bad Request scenarios caused by client input or invalid transitions.
package com.example.transactionstarter.transaction.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) { super(message); }
}
