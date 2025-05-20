package dev.ojas.cryptoCurrency.service;

import dev.ojas.cryptoCurrency.config.MinerConfig;
import dev.ojas.cryptoCurrency.dto.Ledger;
import dev.ojas.cryptoCurrency.model.Transaction;
import dev.ojas.cryptoCurrency.model.User;
import dev.ojas.cryptoCurrency.repository.TransactionRepository;
import dev.ojas.cryptoCurrency.repository.UserRepository;
import dev.ojas.cryptoCurrency.utilities.keys.DecodeKeys;
import dev.ojas.cryptoCurrency.utilities.keys.MessageSignVerify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.time.LocalDateTime;

@Service
public class TransactionService {
    private final MinerConfig minerConfig;
    private final MessageSignVerify messageSignVerify;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final DecodeKeys decodeKeys;

    @Autowired
    public TransactionService(MinerConfig minerConfig, MessageSignVerify messageSignVerify,
                              UserRepository userRepository, TransactionRepository transactionRepository,
                              DecodeKeys decodeKeys) {
        this.minerConfig = minerConfig;
        this.messageSignVerify = messageSignVerify;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.decodeKeys = decodeKeys;
    }

    public int getMiningDifficulty() {
        return this.minerConfig.getDifficulty();
    }

//    public String signTransaction(Ledger ledger) {
//
//    }

    public Transaction addTransaction(Ledger ledger) throws Exception {
        Transaction transaction = new Transaction();
        transaction.setPayerId(ledger.getPayerId());
        transaction.setReceiverId(ledger.getReceiverId());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setTransactionAmount(ledger.getAmount());
        transaction.setMinerReward(ledger.getMinerReward());
        String signMessage = ledger.toString() + " " + transaction.getTimestamp().toString();

        transaction.setPayerSignature(messageSignVerify.signMessage(signMessage,
                getPrivateKeyByUserId(ledger.getPayerId())));
        return transactionRepository.save(transaction);
    }

    private PrivateKey getPrivateKeyByUserId(int userId) throws Exception {
        User user = userRepository.getReferenceById(userId);
        return decodeKeys.getPrivateKeyFromBase64(user.getPrivateKey());
    }
}
