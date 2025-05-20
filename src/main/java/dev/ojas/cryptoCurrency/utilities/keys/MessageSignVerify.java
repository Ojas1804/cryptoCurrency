package dev.ojas.cryptoCurrency.utilities.keys;

import java.security.*;
import java.util.Base64;

import org.springframework.stereotype.Component;

@Component
public class MessageSignVerify {
    public String signMessage(String message, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("Dilithium", "BCPQC");
        signature.initSign(privateKey);
        signature.update(message.getBytes());

        byte[] signatureBytes = signature.sign();
        return Base64.getEncoder().encodeToString(signatureBytes);  // Return as Base64 string
    }

    public boolean verifySignature(String message, String signatureBase64, PublicKey publicKey) throws Exception {
        Signature verifier = Signature.getInstance("Dilithium", "BCPQC");
        verifier.initVerify(publicKey);
        verifier.update(message.getBytes());

        byte[] signatureBytes = Base64.getDecoder().decode(signatureBase64);
        return verifier.verify(signatureBytes);
    }

}
