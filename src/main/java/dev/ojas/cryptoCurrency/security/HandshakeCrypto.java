package dev.ojas.cryptoCurrency.security;

import javax.crypto.KeyAgreement;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public final class HandshakeCrypto {

    private HandshakeCrypto() {
    }

    public static String deriveLinkKey(PublicKey remotePublic, PrivateKey localPrivate, String nonceA, String nonceB) {
        try {
            KeyAgreement agreement = KeyAgreement.getInstance("X25519");
            agreement.init(localPrivate);
            agreement.doPhase(remotePublic, true);
            byte[] secret = agreement.generateSecret();

            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret, "HmacSHA256"));
            byte[] linkKey = mac.doFinal((nonceA + ":" + nonceB).getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(linkKey);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Handshake failed", e);
        }
    }
}
