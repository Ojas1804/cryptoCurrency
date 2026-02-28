package dev.ojas.cryptoCurrency.service;

import dev.ojas.cryptoCurrency.dto.NewUserInboundDto;
import dev.ojas.cryptoCurrency.dto.NewUserResponseDto;
import dev.ojas.cryptoCurrency.model.User;
import dev.ojas.cryptoCurrency.model.Wallet;
import dev.ojas.cryptoCurrency.repository.UserRepository;
import dev.ojas.cryptoCurrency.utilities.keys.DilithiumKeyPair;
import dev.ojas.cryptoCurrency.utilities.keys.PQCKeyGeneration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PQCKeyGeneration pqcKeyGeneration;
    private final WalletService walletService;

    // Constructor injection
    @Autowired
    public UserService(UserRepository userRepository, PQCKeyGeneration pqcKeyGeneration,
                       WalletService walletService) {
        this.userRepository = userRepository;
        this.pqcKeyGeneration = pqcKeyGeneration;
        this.walletService = walletService;
    }

    // Get user by ID
    public Optional<User> getUserById(Integer userId) {
        return userRepository.findById(userId);
    }

    // Get paginated list of users
    public Page<User> getAllUsers(int pageNumber, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        return userRepository.findAll(pageRequest);
    }

    // Create or update a user
    public NewUserResponseDto saveUser(NewUserInboundDto userDto) throws Exception {
        DilithiumKeyPair userKeys = pqcKeyGeneration.generateKeys();
        User user = new User();
        user.setUserName(userDto.getUserName());
        user.setPublicKey(userKeys.getPublicKeyBase64());
        user.setPrivateKey(userKeys.getPrivateKeyBase64());
        user.setTimestampOfCreation(LocalDateTime.now());
//        System.out.println(userKeys.getPrivateKeyBase64().length());

        user = userRepository.save(user);
        Wallet wallet = walletService.createNewWallet(user, userDto.getWalletPassword());
        return new NewUserResponseDto(user.getUserId(), user.getUserName(), user.getPublicKey(),
                wallet.getWalletId(), wallet.getWalletValue(), user.getTimestampOfCreation());
    }

    // Delete a user
    public void deleteUserById(Integer userId) {
        userRepository.deleteById(userId);
    }
}
