package dev.ojas.cryptoCurrency.dto;

public record FileShareRequest(
        String sender,
        String recipient,
        String fileName,
        String contentBase64
) {
}
