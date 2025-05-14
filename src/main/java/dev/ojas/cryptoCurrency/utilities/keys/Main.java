package dev.ojas.cryptoCurrency.utilities.keys;

public class Main {
    public static void main(String[] args) throws Exception {
        DilithiumKeyPair dilithiumKeyPair = PQCKeyGeneration.generateKeys();
        String message = "This is a secret message signed by Ojas";
        String signedMessage = MessageSignVerify.signMessage(message,
                DecodeKeys.getPrivateKeyFromBase64(dilithiumKeyPair.privateKeyBase64));

        System.out.println(signedMessage);

        boolean verified = MessageSignVerify.verifySignature(message, signedMessage,
                DecodeKeys.getPublicKeyFromBase64(dilithiumKeyPair.publicKeyBase64));
        System.out.println(verified);
    }
}
