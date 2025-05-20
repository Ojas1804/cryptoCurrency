package dev.ojas.cryptoCurrency.utilities.keys;

import lombok.Getter;

@Getter
public class DilithiumKeyPair {
    private String publicKeyBase64;
    private String privateKeyBase64;

    public DilithiumKeyPair(String publicKey, String privateKey) {
        this.publicKeyBase64 = publicKey;
        this.privateKeyBase64 = privateKey;
    }
}
