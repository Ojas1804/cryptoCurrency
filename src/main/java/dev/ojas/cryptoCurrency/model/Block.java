package dev.ojas.cryptoCurrency.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BLOCK")
public class Block {
    @Id
    @Column(name = "BLOCK_HASH", columnDefinition = "BINARY(32)")
    private byte[] blockHash;

    @Column(name = "BLOCK_NUMBER", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer blockNumber;

    @Column(name = "PREVIOUS_HASH", columnDefinition = "BINARY(32)")
    private byte[] previousHash;

    @Column(name = "NONCE", nullable = false)
    private int nonce;

    @Column(name = "TIMESTAMP")
    private LocalDateTime timestamp = LocalDateTime.now();
}
