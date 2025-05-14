package dev.ojas.cryptoCurrency.utilities.keys;

public class DilithiumKeyPair {
    public String publicKeyBase64;
    public String privateKeyBase64;

    public DilithiumKeyPair(String publicKey, String privateKey) {
        this.publicKeyBase64 = publicKey;
        this.privateKeyBase64 = privateKey;
    }
}
