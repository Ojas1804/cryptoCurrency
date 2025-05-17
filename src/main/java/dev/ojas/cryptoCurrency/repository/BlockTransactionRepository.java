package dev.ojas.cryptoCurrency.repository;

import dev.ojas.cryptoCurrency.model.BlockTransaction;
import dev.ojas.cryptoCurrency.model.id.BlockTransactionId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BlockTransactionRepository extends JpaRepository<BlockTransaction, BlockTransactionId> {

    // Find by composite key
    // Optional<BlockTransaction> findById(BlockTransactionId id); ← already exists

    // Find all transactions for a specific block
    List<BlockTransaction> findById_BlockId(byte[] blockId);
}
