package dev.ojas.cryptoCurrency.repository;

import dev.ojas.cryptoCurrency.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletRepository extends JpaRepository<Wallet, Integer> {

    // Find by user ID (already exists because Wallet ID == USER_ID)
    // Optional<Wallet> findById(Integer userId);
    List<Wallet> findByUserId(int userId);
}
