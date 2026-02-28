package dev.ojas.cryptoCurrency;

import dev.ojas.cryptoCurrency.dto.FileShareRequest;
import dev.ojas.cryptoCurrency.dto.MessageRequest;
import dev.ojas.cryptoCurrency.model.Block;
import dev.ojas.cryptoCurrency.model.CommunicationPayload;
import dev.ojas.cryptoCurrency.model.TransactionType;
import dev.ojas.cryptoCurrency.service.BlockchainService;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BlockchainServiceTests {

    private final BlockchainService service = new BlockchainService();

    @Test
    void shouldQueueAndMineTransactions() {
        service.addMessage(new MessageRequest("alice", "bob", "hello bob"));
        service.addFile(new FileShareRequest(
                "alice",
                "bob",
                "notes.txt",
                Base64.getEncoder().encodeToString("file-content".getBytes(StandardCharsets.UTF_8))
        ));

        Block minedBlock = service.minePendingTransactions("miner-1");

        assertEquals(1L, minedBlock.index());
        assertEquals(2, service.getChain().size());
        assertTrue(service.getPendingTransactions().isEmpty());
        assertTrue(service.isChainValid());

        List<CommunicationPayload> inbox = service.getInbox("bob");
        assertEquals(2, inbox.size());
        assertTrue(inbox.stream().anyMatch(tx -> tx.type() == TransactionType.FILE));
    }

    @Test
    void shouldRejectInvalidBase64File() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.addFile(new FileShareRequest("alice", "bob", "bad.txt", "notbase64")));

        assertEquals("contentBase64 must contain valid base64", ex.getMessage());
    }
}
