package dev.ojas.cryptoCurrency.repository;

import dev.ojas.cryptoCurrency.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    // Find by ID (inherited)
    // Optional<Transaction> findById(Integer id);

    // Paginated transactions
    Page<Transaction> findAll(Pageable pageable);
}
