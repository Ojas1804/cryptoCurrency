package dev.ojas.cryptoCurrency.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
    @Column(name = "BLOCK_ID", columnDefinition = "BINARY(32)")
    private byte[] blockId;

    @Column(name = "TRANSACTION_ID")
    private Integer transactionId;
}
