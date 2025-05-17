package dev.ojas.cryptoCurrency.model;

import jakarta.persistence.*;

@Entity
@Table(name = "WALLET")
public class Wallet {

    @Id
    @Column(name = "USER_ID")
    private Integer userId;

    @Column(name = "WALLET_VALUE", nullable = false)
    private float walletValue = 0;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    private User user;
}