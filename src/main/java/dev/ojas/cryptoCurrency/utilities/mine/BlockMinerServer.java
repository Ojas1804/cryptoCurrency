package dev.ojas.cryptoCurrency.utilities.mine;

import dev.ojas.cryptoCurrency.utilities.hash.MerkleTree;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Getter
//@Component
public class BlockMinerServer {
    @Getter
    private static int nonce = 0;
    private final String blockHash;

    public BlockMinerServer(int difficulty, Set<String> messages) {
        blockHash = mine(difficulty, messages);
    }

    private static String mine(int difficulty, Set<String> messages) {
//        long nonce = 0;
        String leadingZeros = "0".repeat(difficulty);
        String hashedMessage = "START";
        boolean firstIteration = true;
        Set<String> modifiableMessagesSet = new HashSet<>(messages);
        while (!hashedMessage.startsWith(leadingZeros)) {
            if (!firstIteration) {
                modifiableMessagesSet.remove(String.valueOf(nonce));
                nonce++;
            }
            modifiableMessagesSet.add(String.valueOf(nonce));
            MerkleTree merkleTree = new MerkleTree(modifiableMessagesSet);
            hashedMessage = (String) merkleTree.buildMerkleTree();
            firstIteration = false;
        }
        return hashedMessage;
    }

    public static void main(String[] args) {
        Set<String> messages = Set.of("Message 1", "Message 2", "Message 3", "Message 4");
        BlockMinerServer blockMinerServer = new BlockMinerServer(2, messages);
        System.out.println(blockMinerServer.getBlockHash());
        System.out.println(getNonce());
    }
}
