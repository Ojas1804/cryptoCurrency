package dev.ojas.cryptoCurrency.security;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

import static org.junit.jupiter.api.Assertions.*;

class SecurityComponentsTests {

    @Test
    void passwordHashingShouldVerify() {
        PasswordHashing.HashResult hashResult = PasswordHashing.hashPassword("strong-pass");
        assertTrue(PasswordHashing.verify("strong-pass", hashResult.saltBase64(), hashResult.hashBase64()));
        assertFalse(PasswordHashing.verify("wrong-pass", hashResult.saltBase64(), hashResult.hashBase64()));
    }

    @Test
    void encryptedPrivateKeyShouldLoadBack() throws Exception {
        Path dir = Files.createTempDirectory("keys-test");
        LocalKeyManager keyManager = new LocalKeyManager(dir);

        LocalKeyManager.IdentityKeyPair generated = keyManager.createAndStoreIdentity("alice", "pass123");
        LocalKeyManager.IdentityKeyPair loaded = keyManager.loadIdentity("alice", "pass123");

        assertArrayEquals(generated.publicKey().getEncoded(), loaded.publicKey().getEncoded());
        assertArrayEquals(generated.privateKey().getEncoded(), loaded.privateKey().getEncoded());
    }

    @Test
    void handshakeShouldDeriveSameLinkKeyOnBothSides() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("X25519");
        KeyPair alice = generator.generateKeyPair();
        KeyPair bob = generator.generateKeyPair();

        String nonceA = "nonce-a";
        String nonceB = "nonce-b";

        String aliceDerived = HandshakeCrypto.deriveLinkKey(bob.getPublic(), alice.getPrivate(), nonceA, nonceB);
        String bobDerived = HandshakeCrypto.deriveLinkKey(alice.getPublic(), bob.getPrivate(), nonceA, nonceB);

        assertEquals(aliceDerived, bobDerived);
    }
}
