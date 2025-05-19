package dev.ojas.cryptoCurrency.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@Table(name = "USER")
public class User {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private int userId;
    @Getter
    @Column(name = "USER_NAME")
    private String userName;
    @Getter
    @Column(name = "TIMESTAMP")
    private LocalDateTime timestampOfCreation;
    @Getter
    @Column(name = "PUBLIC_KEY")
    private String publicKey;
    @Column(name = "PRIVATE_KEY")
    private String privateKey;
}
