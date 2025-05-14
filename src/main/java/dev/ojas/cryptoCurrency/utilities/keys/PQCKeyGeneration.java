package dev.ojas.cryptoCurrency.utilities.keys;

import org.bouncycastle.pqc.jcajce.spec.DilithiumParameterSpec;
import org.bouncycastle.pqc.jcajce.provider.BouncyCastlePQCProvider;

import java.security.*;
import java.util.Base64;

public class PQCKeyGeneration {
    public static DilithiumKeyPair generateKeys() throws Exception {
        Security.addProvider(new BouncyCastlePQCProvider());

        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("Dilithium", "BCPQC");
        keyPairGenerator.initialize(DilithiumParameterSpec.dilithium2, new SecureRandom());

        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        String publicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        String privateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

        return new DilithiumKeyPair(publicKey, privateKey);

        // Use keyPair.getPublic() and keyPair.getPrivate() as needed
    }
}
