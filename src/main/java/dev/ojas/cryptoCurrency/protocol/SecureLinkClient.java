package dev.ojas.cryptoCurrency.protocol;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ojas.cryptoCurrency.security.HandshakeCrypto;
import dev.ojas.cryptoCurrency.security.LocalKeyManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

public class SecureLinkClient {

    private final String host;
    private final int port;
    private final ObjectMapper mapper = new ObjectMapper();
    private final LocalKeyManager keyManager;

    public SecureLinkClient(String host, int port, Path localSecureStore) {
        this.host = host;
        this.port = port;
        this.keyManager = new LocalKeyManager(localSecureStore);
    }

    public String registerAndGetPublicKey(String username, String password) throws Exception {
        LocalKeyManager.IdentityKeyPair keys = keyManager.createAndStoreIdentity(username, password);
        return Base64.getEncoder().encodeToString(keys.publicKey().getEncoded());
    }

    public String deriveSharedLinkKey(String username, String password, String remotePublicKeyBase64, String nonceA, String nonceB) throws Exception {
        LocalKeyManager.IdentityKeyPair localKeys = keyManager.loadIdentity(username, password);
        PublicKey remote = KeyFactory.getInstance("X25519")
                .generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(remotePublicKeyBase64)));
        return HandshakeCrypto.deriveLinkKey(remote, localKeys.privateKey(), nonceA, nonceB);
    }

    public void connectAndRun(String username, String password) throws IOException {
        try (Socket socket = new Socket(host, port);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
             BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {

            System.out.println(reader.readLine());

            String loginPayload = toJson(new ProtocolEnvelope("LOGIN", username, password, null, null, null, null, null, null, null, null, null));
            writer.println(loginPayload);
            System.out.println(reader.readLine());

            while (true) {
                String cmd = stdin.readLine();
                if (cmd == null || "exit".equalsIgnoreCase(cmd)) {
                    return;
                }

                if (cmd.startsWith("link ")) {
                    String to = cmd.substring(5).trim();
                    ProtocolEnvelope init = new ProtocolEnvelope("INIT_LINK", null, null, username, to, null, null, UUID.randomUUID().toString(), null, null, null, null);
                    writer.println(toJson(init));
                }
                System.out.println(reader.readLine());
            }
        }
    }

    private String toJson(ProtocolEnvelope message) throws IOException {
        return mapper.writeValueAsString(message);
    }
}
