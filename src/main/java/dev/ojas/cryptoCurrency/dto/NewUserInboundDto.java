package dev.ojas.cryptoCurrency.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewUserInboundDto {
    String userName;
    String walletPassword;
}
