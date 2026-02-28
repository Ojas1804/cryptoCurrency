package dev.ojas.cryptoCurrency.model;

import dev.ojas.cryptoCurrency.model.id.BlockTransactionId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "BLOCK_TRANSACTION")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlockTransaction {
    @EmbeddedId
    private BlockTransactionId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BLOCK_ID", referencedColumnName = "BLOCK_HASH", insertable = false, updatable = false)
    private Block block;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRANSACTION_ID", referencedColumnName = "TRANSACTION_ID", insertable = false, updatable = false)
    private Transaction transaction;
}
