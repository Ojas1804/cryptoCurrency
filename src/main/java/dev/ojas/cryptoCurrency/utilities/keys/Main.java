package dev.ojas.cryptoCurrency.utilities.keys;

public class Main {
    public static void main(String[] args) throws Exception {
        PQCKeyGeneration pqcKeyGeneration = new PQCKeyGeneration();
        DilithiumKeyPair dilithiumKeyPair = pqcKeyGeneration.generateKeys();
        String message = "This is a secret message signed by Ojas";
        String signedMessage = MessageSignVerify.signMessage(message,
                DecodeKeys.getPrivateKeyFromBase64(dilithiumKeyPair.getPrivateKeyBase64()));

        System.out.println(signedMessage);

        boolean verified = MessageSignVerify.verifySignature(message, signedMessage,
                DecodeKeys.getPublicKeyFromBase64(dilithiumKeyPair.getPublicKeyBase64()));
        System.out.println(verified);
    }
}
