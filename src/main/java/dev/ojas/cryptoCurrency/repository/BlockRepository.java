package dev.ojas.cryptoCurrency.repository;

import dev.ojas.cryptoCurrency.model.Block;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockRepository extends JpaRepository<Block, byte[]> {

    // Find by block hash (already inherited)
    // Optional<Block> findById(byte[] blockHash); ← already exists

    // Paginated list of blocks
    Page<Block> findAll(Pageable pageable);
}
