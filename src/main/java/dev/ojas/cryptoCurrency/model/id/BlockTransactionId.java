package dev.ojas.cryptoCurrency.model.id;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

@Embeddable
public class BlockTransactionId implements Serializable {

    @Column(name = "BLOCK_ID", columnDefinition = "BINARY(32)")
    private byte[] blockId;

    @Column(name = "TRANSACTION_ID")
    private Integer transactionId;

    // Default constructor
    public BlockTransactionId() {}

    // Constructor
    public BlockTransactionId(byte[] blockId, Integer transactionId) {
        this.blockId = blockId;
        this.transactionId = transactionId;
    }

    // Getters and Setters
    public byte[] getBlockId() {
        return blockId;
    }

    public void setBlockId(byte[] blockId) {
        this.blockId = blockId;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Integer transactionId) {
        this.transactionId = transactionId;
    }

    // Override equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlockTransactionId)) return false;
        BlockTransactionId that = (BlockTransactionId) o;
        return Arrays.equals(blockId, that.blockId) &&
                Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(blockId), transactionId);
    }
}