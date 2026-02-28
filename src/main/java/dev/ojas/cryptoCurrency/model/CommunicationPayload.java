package dev.ojas.cryptoCurrency.model;

import lombok.Builder;

import java.time.Instant;

@Builder
public record CommunicationPayload(
        String id,
        TransactionType type,
        String sender,
        String recipient,
        String body,
        String fileName,
        String checksum,
        Instant createdAt
) {
}
