package dev.ojas.cryptoCurrency.dto;

public record MessageRequest(
        String sender,
        String recipient,
        String message
) {
}
