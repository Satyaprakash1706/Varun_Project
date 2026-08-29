// Spring Data JPA repository for Transaction entities. Provides a convenience finder for customer transactions.
package com.example.transactionstarter.transaction;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByCustomerId(String customerId);
}
