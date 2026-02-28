package dev.ojas.cryptoCurrency.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "WALLET")
@Setter
@Getter
public class Wallet {

    @Id
    @Column(name = "WALLET_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int walletId;

    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "WALLET_VALUE", nullable = false)
    private float walletValue = 0;

    @Column(name = "PASSWORD", nullable = false, columnDefinition = "BINARY(32)")
    private String password;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    private User user;
}