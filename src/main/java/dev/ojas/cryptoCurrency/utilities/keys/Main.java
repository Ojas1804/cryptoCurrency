package dev.ojas.cryptoCurrency.utilities.keys;

public class Main {
    public static void main(String[] args) throws Exception {
        PQCKeyGeneration pqcKeyGeneration = new PQCKeyGeneration();
        DilithiumKeyPair dilithiumKeyPair = pqcKeyGeneration.generateKeys();
        DecodeKeys decodeKeys = new DecodeKeys();
        String message = "This is a secret message signed by Ojas";
        MessageSignVerify messageSignVerify = new MessageSignVerify();
        String signedMessage = messageSignVerify.signMessage(message,
                decodeKeys.getPrivateKeyFromBase64(dilithiumKeyPair.getPrivateKeyBase64()));

        System.out.println(signedMessage);

        boolean verified = messageSignVerify.verifySignature(message, signedMessage,
                decodeKeys.getPublicKeyFromBase64(dilithiumKeyPair.getPublicKeyBase64()));
        System.out.println(verified);
    }
}
