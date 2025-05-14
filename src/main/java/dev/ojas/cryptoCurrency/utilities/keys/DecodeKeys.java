package dev.ojas.cryptoCurrency.utilities.keys;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class DecodeKeys {
    public static PublicKey getPublicKeyFromBase64(String base64PublicKey) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(base64PublicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("Dilithium", "BCPQC");
        return keyFactory.generatePublic(spec);
    }

    public static PrivateKey getPrivateKeyFromBase64(String base64PrivateKey) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(base64PrivateKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("Dilithium", "BCPQC");
        return keyFactory.generatePrivate(spec);
    }
}
