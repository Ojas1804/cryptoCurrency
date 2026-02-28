package dev.ojas.cryptoCurrency.service;

import dev.ojas.cryptoCurrency.dto.FileShareRequest;
import dev.ojas.cryptoCurrency.dto.MessageRequest;
import dev.ojas.cryptoCurrency.model.Block;
import dev.ojas.cryptoCurrency.model.CommunicationPayload;
import dev.ojas.cryptoCurrency.model.TransactionType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BlockchainService {

    private static final int DIFFICULTY = 3;

    private final List<Block> chain = new ArrayList<>();
    private final List<CommunicationPayload> pendingTransactions = new ArrayList<>();

    public BlockchainService() {
        Block genesis = mineBlock(List.of(
                CommunicationPayload.builder()
                        .id(UUID.randomUUID().toString())
                        .type(TransactionType.MESSAGE)
                        .sender("network")
                        .recipient("all")
                        .body("Genesis block for secure communication network")
                        .fileName(null)
                        .checksum(sha256("genesis"))
                        .createdAt(Instant.now())
                        .build()
        ), "0", 0L);
        chain.add(genesis);
    }

    public synchronized CommunicationPayload addMessage(MessageRequest request) {
        require(request.sender(), "sender");
        require(request.recipient(), "recipient");
        require(request.message(), "message");

        CommunicationPayload payload = CommunicationPayload.builder()
                .id(UUID.randomUUID().toString())
                .type(TransactionType.MESSAGE)
                .sender(request.sender())
                .recipient(request.recipient())
                .body(request.message())
                .fileName(null)
                .checksum(sha256(request.message()))
                .createdAt(Instant.now())
                .build();

        pendingTransactions.add(payload);
        return payload;
    }

    public synchronized CommunicationPayload addFile(FileShareRequest request) {
        require(request.sender(), "sender");
        require(request.recipient(), "recipient");
        require(request.fileName(), "fileName");
        require(request.contentBase64(), "contentBase64");

        if (!isBase64(request.contentBase64())) {
            throw new IllegalArgumentException("contentBase64 must contain valid base64");
        }

        CommunicationPayload payload = CommunicationPayload.builder()
                .id(UUID.randomUUID().toString())
                .type(TransactionType.FILE)
                .sender(request.sender())
                .recipient(request.recipient())
                .body(request.contentBase64())
                .fileName(request.fileName())
                .checksum(sha256(request.contentBase64()))
                .createdAt(Instant.now())
                .build();

        pendingTransactions.add(payload);
        return payload;
    }

    public synchronized Block minePendingTransactions(String minerName) {
        require(minerName, "miner");

        if (pendingTransactions.isEmpty()) {
            throw new IllegalStateException("No pending transactions to mine");
        }

        List<CommunicationPayload> batch = new ArrayList<>(pendingTransactions);
        pendingTransactions.clear();

        CommunicationPayload reward = CommunicationPayload.builder()
                .id(UUID.randomUUID().toString())
                .type(TransactionType.MESSAGE)
                .sender("network")
                .recipient(minerName)
                .body("Mining reward: reputation +1")
                .checksum(sha256("reward-" + minerName + "-" + Instant.now()))
                .createdAt(Instant.now())
                .build();

        batch.add(reward);
        Block previous = chain.get(chain.size() - 1);
        Block next = mineBlock(batch, previous.hash(), previous.index() + 1);
        chain.add(next);
        return next;
    }

    public synchronized List<Block> getChain() {
        return List.copyOf(chain);
    }

    public synchronized List<CommunicationPayload> getInbox(String recipient) {
        require(recipient, "recipient");

        return chain.stream()
                .flatMap(block -> block.transactions().stream())
                .filter(tx -> recipient.equalsIgnoreCase(tx.recipient()))
                .sorted(Comparator.comparing(CommunicationPayload::createdAt))
                .toList();
    }

    public synchronized List<CommunicationPayload> getSharedFiles(String recipient) {
        return getInbox(recipient).stream()
                .filter(tx -> tx.type() == TransactionType.FILE)
                .collect(Collectors.toList());
    }

    public synchronized boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block current = chain.get(i);
            Block previous = chain.get(i - 1);

            String recalculated = calculateHash(current.index(), current.timestamp(), current.previousHash(), current.nonce(), current.transactions());

            if (!current.hash().equals(recalculated)) {
                return false;
            }

            if (!current.previousHash().equals(previous.hash())) {
                return false;
            }

            if (!current.hash().startsWith("0".repeat(DIFFICULTY))) {
                return false;
            }
        }
        return true;
    }

    public synchronized List<CommunicationPayload> getPendingTransactions() {
        return List.copyOf(pendingTransactions);
    }

    private Block mineBlock(List<CommunicationPayload> txs, String previousHash, long index) {
        long nonce = 0L;
        Instant now = Instant.now();
        String hash;

        do {
            hash = calculateHash(index, now, previousHash, nonce, txs);
            nonce++;
        } while (!hash.startsWith("0".repeat(DIFFICULTY)));

        return Block.builder()
                .index(index)
                .timestamp(now)
                .previousHash(previousHash)
                .nonce(nonce - 1)
                .transactions(List.copyOf(txs))
                .hash(hash)
                .build();
    }

    private String calculateHash(long index, Instant timestamp, String previousHash, long nonce, List<CommunicationPayload> txs) {
        String txBody = txs.stream()
                .map(tx -> tx.id() + tx.type() + tx.sender() + tx.recipient() + tx.body() + tx.fileName() + tx.checksum() + tx.createdAt())
                .collect(Collectors.joining("|"));

        return sha256(index + ":" + timestamp + ":" + previousHash + ":" + nonce + ":" + txBody);
    }

    private String sha256(Object input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(String.valueOf(input).getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }

    private void require(String value, String field) {
        if (!StringUtils.hasText(value)) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
    }

    private boolean isBase64(String value) {
        try {
            Base64.getDecoder().decode(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
