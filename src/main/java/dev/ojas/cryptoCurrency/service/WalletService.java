package dev.ojas.cryptoCurrency.service;

import dev.ojas.cryptoCurrency.model.User;
import dev.ojas.cryptoCurrency.model.Wallet;
import dev.ojas.cryptoCurrency.repository.WalletRepository;
import dev.ojas.cryptoCurrency.utilities.hash.MerkleTree;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository, MerkleTree merkleTree) {
        this.walletRepository = walletRepository;
    }

    public Wallet createNewWallet(User user, String password) {
        Wallet wallet = new Wallet();
        wallet.setUserId(user.getUserId());
        wallet.setPassword(MerkleTree.pqcHash(password));
        wallet.setWalletValue(0.0F);
        wallet.setUser(user);
        return walletRepository.save(wallet);
    }
}
