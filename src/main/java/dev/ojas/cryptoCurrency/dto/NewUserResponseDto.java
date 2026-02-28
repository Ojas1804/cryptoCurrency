package dev.ojas.cryptoCurrency.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NewUserResponseDto {
    private int userId;
    private String userName;
    private String publicKeyBase64;
    private int walletId;
    private float walletValue;
    private LocalDateTime timestampOfCreation;

}
