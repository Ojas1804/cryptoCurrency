package dev.ojas.cryptoCurrency.protocol;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProtocolEnvelope(
        String type,
        String username,
        String password,
        String from,
        String to,
        String linkId,
        String publicKey,
        String nonce,
        String payload,
        String fileName,
        String status,
        String message
) {
    public static ProtocolEnvelope error(String message) {
        return new ProtocolEnvelope("ERROR", null, null, null, null, null, null, null, null, null, "FAILED", message);
    }

    public static ProtocolEnvelope ok(String type, String message) {
        return new ProtocolEnvelope(type, null, null, null, null, null, null, null, null, null, "OK", message);
    }
}
