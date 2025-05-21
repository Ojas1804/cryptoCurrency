package dev.ojas.cryptoCurrency.utilities.hash;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;

@Component
public class HashFunction {
    // A simple hash function using SHA-256 (you'd replace this with a PQC hash in production)
    public String sha256Hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error hashing input", e);
        }
    }

    public String pqcHash(String input) {
        // This is a dummy PQC hash function for illustration.
        // In practice, you would use an actual PQC algorithm like SPHINCS+ or Keccak.

        // Simple emulation of a PQC hash (e.g., from a post-quantum scheme like SPHINCS+ or Keccak)
        try {
            // This would be replaced by a real PQC hashing algorithm
            MessageDigest digest = MessageDigest.getInstance("SHA3-256");  // Keccak (SHA-3) is a candidate.
            byte[] hashBytes = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error hashing input with PQC hash function", e);
        }
    }
}
