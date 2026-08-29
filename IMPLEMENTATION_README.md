Implementation README

What was implemented
- REST API for transactions with endpoints:
  - POST /api/transactions - create transaction (server assigns UUID, initial status PENDING)
  - GET /api/transactions/{id} - get transaction by id
  - PATCH /api/transactions/{id}/status - update transaction status (only PENDING -> COMPLETED|FAILED)
  - GET /api/customers/{customerId}/transactions - list transactions for a customer

Validation rules
- customerId: must be present (non-blank)
- amount: must be >= 0.01
- currency: 3-letter ISO code (normalized to uppercase)
- type: DEBIT or CREDIT
- initial status: server forces PENDING on create

Status transition rules
- Allowed: PENDING -> COMPLETED
- Allowed: PENDING -> FAILED
- Disallowed: any change when current status is COMPLETED or FAILED
- Disallowed: transition back to PENDING

Error handling
- 400 for validation or invalid transitions
- 404 when transaction not found
- 500 for unexpected errors

How to run
- On Windows: mvnw.cmd clean test
- On Unix/macOS: ./mvnw clean test

Tests added
- createAndGetTransaction
- updateStatusTransitions (valid and invalid transition)
- getCustomerTransactions
- validationRejectsBadAmountAndCurrency

Notes
- Business rule: initial status is always PENDING regardless of client-supplied status to keep state consistent.
