# Secure Link Messenger (Java, No HTTP)

This project implements a **custom TCP protocol** (not REST/HTTP) for secure communication and file sharing.

## Core requirements implemented

- Uses a custom socket protocol over **port 8082** on every device.
- No API endpoints are used for communication.
- Creates user-to-user communication links through a handshake flow.
- Generates cryptographic keys for communication using X25519.
- Stores credentials and private keys locally in a safer way:
    - passwords hashed with PBKDF2-HMAC-SHA256 + per-user salt
    - private keys encrypted with AES-GCM using a password-derived key

## Protocol overview (line-delimited JSON)

Connection transport: `TCP`, default server port `8082`.

Main message types:

- `REGISTER` – register user with local-safe password hash and public key.
- `LOGIN` – authenticate user.
- `INIT_LINK` – initiator asks to create a communication link.
- `LINK_REQUEST` – server relays link/handshake request to recipient.
- `HANDSHAKE_RESPONSE` – recipient sends handshake response.
- `HANDSHAKE_FINAL` – initiator finalizes handshake.
- `MESSAGE` – encrypted text payload relay.
- `FILE` – encrypted file payload relay.

## Local secure storage

Base local directory:

- `~/.secure-link-messenger/users.json` (salted password hashes + public keys)
- `~/.secure-link-messenger/keys/<username>.key` (AES-GCM encrypted private key blob)

## Handshake model

1. User A creates link request with nonceA and sends `INIT_LINK`.
2. User B receives `LINK_REQUEST` and returns `HANDSHAKE_RESPONSE` with nonceB and public key.
3. Both derive a shared link key with X25519 ECDH + HMAC(nonceA:nonceB).
4. Link key is then used by clients to encrypt/decrypt message/file payloads before relay.

## Run

```bash
./mvnw spring-boot:run
```

Server starts the custom protocol listener on port `8082`.
