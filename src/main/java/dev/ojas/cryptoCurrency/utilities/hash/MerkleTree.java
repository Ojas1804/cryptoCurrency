package dev.ojas.cryptoCurrency.utilities.hash;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

@Getter
@Component
public class MerkleTree {

    // Getter for the Merkle tree root
    private Set<String> leaves;
    private final HashFunction hashFunction;
    private Function<String, String> hash;

    // Constructor that takes the list of messages and creates a Merkle tree.
    @Autowired
    public MerkleTree(HashFunction hashFunction) {
        this.hashFunction = hashFunction;
//        root = buildMerkleTree(leaves);
    }

    private void init() {
        this.leaves = new HashSet<>();
        this.hash = this.hashFunction::pqcHash;
    }

    // Method to build the Merkle tree and return the root hash
    public String buildMerkleTree(Set<String> messages) {
        for (String message : messages) {
            this.leaves.add(hash.apply(message));  // Hash each message to create leaf nodes
        }

        List<String> nodesList = new ArrayList<>(this.leaves);
        while (nodesList.size() > 1) {
            List<String> newLevel = new ArrayList<>();
            for (int i = 0; i < nodesList.size(); i += 2) {
                // Pair up adjacent nodes and hash them together to form the next level
                if (i + 1 < nodesList.size()) newLevel.add(hash.apply(nodesList.get(i) +
                        nodesList.get(i + 1)));  // Concatenate and hash
                else newLevel.add(nodesList.get(i));  // For odd number of nodes, carry the last one forward
            }
            nodesList = newLevel;  // Move up to the next level
        }
        return nodesList.get(0);  // The remaining node is the Merkle root
    }

//    // A simple hash function using SHA-256 (you'd replace this with a PQC hash in production)
//    private static String sha256Hash(String input) {
//        try {
//            MessageDigest digest = MessageDigest.getInstance("SHA-256");
//            byte[] hashBytes = digest.digest(input.getBytes());
//            StringBuilder hexString = new StringBuilder();
//            for (byte b : hashBytes) {
//                hexString.append(String.format("%02x", b));
//            }
//            return hexString.toString();
//        } catch (Exception e) {
//            throw new RuntimeException("Error hashing input", e);
//        }
//    }
//
//    public static String pqcHash(String input) {
//        // This is a dummy PQC hash function for illustration.
//        // In practice, you would use an actual PQC algorithm like SPHINCS+ or Keccak.
//
//        // Simple emulation of a PQC hash (e.g., from a post-quantum scheme like SPHINCS+ or Keccak)
//        try {
//            // This would be replaced by a real PQC hashing algorithm
//            MessageDigest digest = MessageDigest.getInstance("SHA3-256");  // Keccak (SHA-3) is a candidate.
//            byte[] hashBytes = digest.digest(input.getBytes());
//            StringBuilder hexString = new StringBuilder();
//            for (byte b : hashBytes) {
//                hexString.append(String.format("%02x", b));
//            }
//            return hexString.toString();
//        } catch (Exception e) {
//            throw new RuntimeException("Error hashing input with PQC hash function", e);
//        }
//    }

    // Main method for testing
//    public static void main(String[] args) {
//        Set<String> messages = new HashSet<>();
//        messages.add("Message 1");
//        messages.add("Message 2");
//        messages.add("Message 3");
//        messages.add("Message 4");
//
//        MerkleTree tree = new MerkleTree();
//        System.out.println("Merkle Root: " + tree.buildMerkleTree());
//    }
}
