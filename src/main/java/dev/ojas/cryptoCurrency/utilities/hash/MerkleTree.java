package dev.ojas.cryptoCurrency.utilities.hash;

import lombok.Getter;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
public class MerkleTree {

    // Getter for the Merkle tree root
    private final String root;
    private final Function<String, String> hash = MerkleTree::sha256Hash;

    // Constructor that takes the list of messages and creates a Merkle tree.
    public MerkleTree(List<String> messages) {
        List<String> leaves = new ArrayList<>();
        for (String message : messages) {
            leaves.add(hash.apply(message));  // Hash each message to create leaf nodes
        }
        root = buildMerkleTree(leaves);
    }

    // Method to build the Merkle tree and return the root hash
    private String buildMerkleTree(List<String> nodes) {
        while (nodes.size() > 1) {
            List<String> newLevel = new ArrayList<>();
            for (int i = 0; i < nodes.size(); i += 2) {
                // Pair up adjacent nodes and hash them together to form the next level
                if (i + 1 < nodes.size()) newLevel.add(hash.apply(nodes.get(i) + nodes.get(i + 1)));  // Concatenate and hash
                else newLevel.add(nodes.get(i));  // For odd number of nodes, carry the last one forward
            }
            nodes = newLevel;  // Move up to the next level
        }
        return nodes.get(0);  // The remaining node is the Merkle root
    }

    // A simple hash function using SHA-256 (you'd replace this with a PQC hash in production)
    private static String sha256Hash(String input) {
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

    private static String pqcHash(String input) {
        // This is a dummy PQC hash function for illustration.
        // In practice, you would use an actual PQC algorithm like SPHINCS+ or Keccak.

        // Simple emulation of a PQC hash (e.g., from a post-quantum scheme like SPHINCS+ or Keccak)
        try {
            // This would be replaced by a real PQC hashing algorithm
            MessageDigest digest = MessageDigest.getInstance("SHA-3-256");  // Keccak (SHA-3) is a candidate.
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

    // Main method for testing
    public static void main(String[] args) {
        List<String> messages = List.of("Message 1", "Message 2", "Message 3", "Message 4");

        MerkleTree tree = new MerkleTree(messages);
        System.out.println("Merkle Root: " + tree.getRoot());
    }
}
