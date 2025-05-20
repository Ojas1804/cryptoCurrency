package dev.ojas.cryptoCurrency.repository;

import dev.ojas.cryptoCurrency.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository extends JpaRepository<User, Integer> {

    // Find a user by their ID (already inherited from JpaRepository)
    // Optional<User> findById(Integer id); ← already exists

    // Get paginated list of users
    Page<User> findAll(Pageable pageable);
}

