package dev.ojas.cryptoCurrency.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ojas.cryptoCurrency.security.PasswordHashing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UserCredentialStore {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Path dbFile;
    private final Map<String, UserRecord> users;

    public UserCredentialStore(Path baseDir) throws IOException {
        Files.createDirectories(baseDir);
        this.dbFile = baseDir.resolve("users.json");

        if (Files.exists(dbFile)) {
            this.users = objectMapper.readValue(dbFile.toFile(), new TypeReference<>() {});
        } else {
            this.users = new HashMap<>();
            save();
        }
    }

    public synchronized boolean register(String username, String password, String publicKey) throws IOException {
        if (users.containsKey(username)) {
            return false;
        }

        PasswordHashing.HashResult hash = PasswordHashing.hashPassword(password);
        users.put(username, new UserRecord(username, hash.saltBase64(), hash.hashBase64(), publicKey));
        save();
        return true;
    }

    public synchronized boolean verifyPassword(String username, String password) {
        UserRecord record = users.get(username);
        return record != null && PasswordHashing.verify(password, record.saltBase64(), record.passwordHashBase64());
    }

    public synchronized Optional<UserRecord> getByUsername(String username) {
        return Optional.ofNullable(users.get(username));
    }

    private void save() throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(dbFile.toFile(), users);
    }

    public record UserRecord(String username, String saltBase64, String passwordHashBase64, String publicKeyBase64) {}
}
