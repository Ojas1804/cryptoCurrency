package dev.ojas.cryptoCurrency.model.id;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class BlockTransactionId implements Serializable {

    @Column(name = "BLOCK_ID", columnDefinition = "BINARY(32)")
    private String blockId;

    @Column(name = "TRANSACTION_ID")
    private Integer transactionId;

    // Default constructor
    public BlockTransactionId() {}

    // Constructor
    public BlockTransactionId(String blockId, Integer transactionId) {
        this.blockId = blockId;
        this.transactionId = transactionId;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlockTransactionId that)) return false;
        return Objects.equals(blockId, that.blockId) &&
                Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockId, transactionId);
    }
}