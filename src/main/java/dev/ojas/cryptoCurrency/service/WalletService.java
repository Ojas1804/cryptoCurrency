package dev.ojas.cryptoCurrency.service;

import dev.ojas.cryptoCurrency.model.User;
import dev.ojas.cryptoCurrency.model.Wallet;
import dev.ojas.cryptoCurrency.repository.WalletRepository;
import dev.ojas.cryptoCurrency.utilities.hash.HashFunction;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
    private final WalletRepository walletRepository;
    private final HashFunction hashFunction;

    public WalletService(WalletRepository walletRepository, HashFunction hashFunction) {
        this.walletRepository = walletRepository;
        this.hashFunction = hashFunction;
    }

    public Wallet createNewWallet(User user, String password) {
        Wallet wallet = new Wallet();
        wallet.setUserId(user.getUserId());
        wallet.setPassword(hashFunction.pqcHash(password));
        wallet.setWalletValue(0.0F);
        wallet.setUser(user);
        return walletRepository.save(wallet);
    }

    public boolean updateWalletValue(int walletId, boolean add, float amount) {
        Wallet wallet = walletRepository.getReferenceById(walletId);
        if(add) wallet.setWalletValue(wallet.getWalletValue() + amount);
        else wallet.setWalletValue(wallet.getWalletValue() - amount);
        return true;
    }

    public boolean verifyPassword(int walletId, String password) {
        String hashedPassword = hashFunction.pqcHash(password);
        Wallet wallet = walletRepository.getReferenceById(walletId);
        return wallet.getPassword().equals(hashedPassword);
    }
}
