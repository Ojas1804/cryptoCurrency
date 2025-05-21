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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    private final MinerConfig minerConfig;
    private final MessageSignVerify messageSignVerify;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final DecodeKeys decodeKeys;
    private final WalletService walletService;

    @Autowired
    public TransactionService(MinerConfig minerConfig, MessageSignVerify messageSignVerify,
                              UserRepository userRepository, TransactionRepository transactionRepository,
                              DecodeKeys decodeKeys, WalletService walletService) {
        this.minerConfig = minerConfig;
        this.messageSignVerify = messageSignVerify;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.decodeKeys = decodeKeys;
        this.walletService = walletService;
    }

    public int getMiningDifficulty() {
        return this.minerConfig.getDifficulty();
    }

    public Transaction addTransaction(Ledger ledger) throws Exception {
        Transaction transaction = new Transaction();
        transaction.setPayerId(ledger.getPayerId());
        transaction.setReceiverId(ledger.getReceiverId());
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setTransactionAmount(ledger.getAmount());
        transaction.setPayerWalletId(ledger.getPayerWalletId());
        transaction.setReceiverWalletId(ledger.getReceiverWalletId());
        transaction.setMinerReward(ledger.getMinerReward());

        boolean verified = walletService.verifyPassword(ledger.getPayerWalletId(),
                ledger.getPayerWalletPassword());
        if(!verified) throw new Exception("Incorrect password");
        else {
            String signMessage = ledger.toString() + " " + transaction.getTimestamp().toString();
            transaction.setPayerSignature(messageSignVerify.signMessage(signMessage,
                    getPrivateKeyByUserId(ledger.getPayerId())));
        }
        transaction = transactionRepository.save(transaction);

        // update payer and receiver wallets with amounts (shoud be done after block is added to the chain)
        walletService.updateWalletValue(transaction.getPayerWalletId(), false, transaction.getTransactionAmount());
        walletService.updateWalletValue(transaction.getReceiverWalletId(), true, transaction.getTransactionAmount());

        return transaction;
    }

    private PrivateKey getPrivateKeyByUserId(int userId) throws Exception {
        User user = userRepository.getReferenceById(userId);
        return decodeKeys.getPrivateKeyFromBase64(user.getPrivateKey());
    }

    public Transaction getTransactionById(int transactionId) {
        return transactionRepository.getReferenceById(transactionId);
    }

    public List<Transaction> getTransactionsForPayerById(int userId) {
        return transactionRepository.findByPayerId(userId);
    }

    public List<Transaction> getTransactionsForReceiverById(int userId) {
        return transactionRepository.findByReceiverId(userId);
    }

    public Page<Transaction> getAllTransaction(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return transactionRepository.findAll(pageRequest);
    }
}
