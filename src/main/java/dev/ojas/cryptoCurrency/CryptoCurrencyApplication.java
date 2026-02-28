package dev.ojas.cryptoCurrency;

import dev.ojas.cryptoCurrency.protocol.PeerProtocolServer;
import dev.ojas.cryptoCurrency.storage.UserCredentialStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.nio.file.Path;

@SpringBootApplication
public class CryptoCurrencyApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptoCurrencyApplication.class, args);
    }

    @Bean
    CommandLineRunner protocolServerRunner() {
        return args -> {
            Path baseDir = Path.of(System.getProperty("user.home"), ".secure-link-messenger");
            UserCredentialStore credentialStore = new UserCredentialStore(baseDir);
            PeerProtocolServer server = new PeerProtocolServer(8082, credentialStore);
            server.start();
        };
    }
}
