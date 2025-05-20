package dev.ojas.cryptoCurrency.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TRANSACTION")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSACTION_ID")
    private long transactionId;

    @Column(name = "PAYER_ID")
    private int payerId;

    @Column(name = "RECEIVER_ID")
    private int receiverId;

    @Column(name = "AMOUNT")
    private float transactionAmount;

    @Column(name = "MINER_REWARD")
    private float minerReward;

    @Column(name = "TIMESTAMP")
    private LocalDateTime timestamp;

    @Column(name = "PAYER_SIGNATURE")
    private String payerSignature;
}
