package dev.ojas.cryptoCurrency.model;

import lombok.Builder;

import java.time.Instant;
import java.util.List;

@Builder
public record Block(
        long index,
        Instant timestamp,
        String previousHash,
        long nonce,
        List<CommunicationPayload> transactions,
        String hash
) {
}
