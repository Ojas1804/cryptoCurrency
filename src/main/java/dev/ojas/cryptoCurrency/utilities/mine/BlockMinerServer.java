package dev.ojas.cryptoCurrency.utilities.mine;

import dev.ojas.cryptoCurrency.utilities.hash.MerkleTree;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Getter
@Component
public class BlockMinerServer {
    @Getter
    private static int nonce = 0;
    private String blockHash;
    private final MerkleTree merkleTree;

    @Autowired
    public BlockMinerServer(MerkleTree merkleTree) {
        this.merkleTree = merkleTree;
    }

    public final void mine(int difficulty, Set<String> messages) {
        System.out.println(difficulty);
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
            hashedMessage = (String) merkleTree.buildMerkleTree(modifiableMessagesSet);
            firstIteration = false;
        }
        this.blockHash = hashedMessage;
    }
}
