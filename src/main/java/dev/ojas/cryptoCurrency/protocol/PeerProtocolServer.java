package dev.ojas.cryptoCurrency.protocol;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ojas.cryptoCurrency.storage.UserCredentialStore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PeerProtocolServer {

    private final int port;
    private final UserCredentialStore credentialStore;
    private final ObjectMapper mapper = new ObjectMapper();
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Map<String, ClientSession> onlineUsers = new ConcurrentHashMap<>();

    public PeerProtocolServer(int port, UserCredentialStore credentialStore) {
        this.port = port;
        this.credentialStore = credentialStore;
    }

    public void start() {
        executor.submit(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                while (!Thread.currentThread().isInterrupted()) {
                    Socket socket = serverSocket.accept();
                    executor.submit(() -> handleClient(socket));
                }
            } catch (IOException e) {
                throw new IllegalStateException("Failed to run protocol server", e);
            }
        });
    }

    private void handleClient(Socket socket) {
        String authenticatedUser = null;
        try (socket;
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8)) {

            writer.println(toJson(ProtocolEnvelope.ok("SERVER_READY", "Secure link protocol ready on port " + port)));

            String line;
            while ((line = reader.readLine()) != null) {
                ProtocolEnvelope request = mapper.readValue(line, ProtocolEnvelope.class);

                switch (request.type()) {
                    case "REGISTER" -> {
                        boolean registered = credentialStore.register(request.username(), request.password(), request.publicKey());
                        if (!registered) {
                            writer.println(toJson(ProtocolEnvelope.error("Username already exists")));
                        } else {
                            writer.println(toJson(ProtocolEnvelope.ok("REGISTERED", "User registered")));
                        }
                    }
                    case "LOGIN" -> {
                        boolean valid = credentialStore.verifyPassword(request.username(), request.password());
                        if (!valid) {
                            writer.println(toJson(ProtocolEnvelope.error("Invalid credentials")));
                        } else {
                            authenticatedUser = request.username();
                            onlineUsers.put(authenticatedUser, new ClientSession(authenticatedUser, writer));
                            writer.println(toJson(ProtocolEnvelope.ok("LOGGED_IN", "Authenticated")));
                        }
                    }
                    case "INIT_LINK" -> {
                        if (!isAuthenticated(authenticatedUser, writer)) {
                            continue;
                        }
                        String linkId = UUID.randomUUID().toString();
                        ProtocolEnvelope created = new ProtocolEnvelope(
                                "LINK_CREATED", null, null, authenticatedUser, request.to(), linkId,
                                null, request.nonce(), null, null, "OK", "Share this link id with recipient"
                        );
                        writer.println(toJson(created));

                        relay(authenticatedUser, request.to(), new ProtocolEnvelope(
                                "LINK_REQUEST", null, null, authenticatedUser, request.to(), linkId,
                                request.publicKey(), request.nonce(), null, null, "PENDING", "Handshake request"
                        ));
                    }
                    case "HANDSHAKE_RESPONSE", "HANDSHAKE_FINAL", "MESSAGE", "FILE" -> {
                        if (!isAuthenticated(authenticatedUser, writer)) {
                            continue;
                        }
                        relay(authenticatedUser, request.to(), request);
                    }
                    default -> writer.println(toJson(ProtocolEnvelope.error("Unsupported type: " + request.type())));
                }
            }
        } catch (Exception e) {
            // connection closed or invalid payload
        } finally {
            if (authenticatedUser != null) {
                onlineUsers.remove(authenticatedUser);
            }
        }
    }

    private boolean isAuthenticated(String username, PrintWriter writer) {
        if (username == null) {
            writer.println(toJson(ProtocolEnvelope.error("Authenticate first with LOGIN")));
            return false;
        }
        return true;
    }

    private void relay(String from, String to, ProtocolEnvelope envelope) {
        ClientSession recipient = onlineUsers.get(to);
        ClientSession sender = onlineUsers.get(from);

        if (recipient == null) {
            if (sender != null) {
                sender.writer().println(toJson(ProtocolEnvelope.error("Recipient is offline")));
            }
            return;
        }

        recipient.writer().println(toJson(envelope));
        if (sender != null) {
            sender.writer().println(toJson(ProtocolEnvelope.ok("RELAYED", "Delivered to " + to)));
        }
    }

    private String toJson(ProtocolEnvelope envelope) {
        try {
            return mapper.writeValueAsString(envelope);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to encode response", e);
        }
    }

    private record ClientSession(String username, PrintWriter writer) {
    }
}
