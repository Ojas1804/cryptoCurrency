package dev.ojas.cryptoCurrency.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class LocalKeyManager {

    private final Path baseDir;

    public LocalKeyManager(Path baseDir) {
        this.baseDir = baseDir;
    }

    public IdentityKeyPair createAndStoreIdentity(String username, String password) throws GeneralSecurityException, IOException {
        Files.createDirectories(baseDir);

        KeyPairGenerator generator = KeyPairGenerator.getInstance("X25519");
        KeyPair keyPair = generator.generateKeyPair();

        byte[] salt = new byte[16];
        byte[] iv = new byte[12];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        random.nextBytes(iv);

        SecretKey aesKey = deriveAesKey(password, salt);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, new GCMParameterSpec(128, iv));
        byte[] encryptedPrivate = cipher.doFinal(keyPair.getPrivate().getEncoded());

        KeyBlob blob = new KeyBlob(
                Base64.getEncoder().encodeToString(salt),
                Base64.getEncoder().encodeToString(iv),
                Base64.getEncoder().encodeToString(encryptedPrivate),
                Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded())
        );

        String data = blob.salt() + "." + blob.iv() + "." + blob.encryptedPrivate() + "." + blob.publicKey();
        Files.writeString(baseDir.resolve(username + ".key"), data, StandardCharsets.UTF_8);

        return new IdentityKeyPair(keyPair.getPublic(), keyPair.getPrivate());
    }

    public IdentityKeyPair loadIdentity(String username, String password) throws IOException, GeneralSecurityException {
        String raw = Files.readString(baseDir.resolve(username + ".key"), StandardCharsets.UTF_8);
        String[] parts = raw.split("\\.");
        if (parts.length != 4) {
            throw new IllegalStateException("Corrupted key file");
        }

        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] iv = Base64.getDecoder().decode(parts[1]);
        byte[] encryptedPrivate = Base64.getDecoder().decode(parts[2]);
        byte[] publicBytes = Base64.getDecoder().decode(parts[3]);

        SecretKey aesKey = deriveAesKey(password, salt);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, aesKey, new GCMParameterSpec(128, iv));
        byte[] privateBytes = cipher.doFinal(encryptedPrivate);

        KeyFactory keyFactory = KeyFactory.getInstance("X25519");
        PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicBytes));
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateBytes));

        return new IdentityKeyPair(publicKey, privateKey);
    }

    private SecretKey deriveAesKey(String password, byte[] salt) throws GeneralSecurityException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 120_000, 256);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    private record KeyBlob(String salt, String iv, String encryptedPrivate, String publicKey) {
    }

    public record IdentityKeyPair(PublicKey publicKey, PrivateKey privateKey) {
    }
}
