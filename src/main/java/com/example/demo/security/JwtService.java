package com.example.demo.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JwtService {

    private static final Base64.Encoder URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder URL_DECODER = Base64.getUrlDecoder();
    private static final String HMAC_ALGORITHM = "HmacSHA256";

    @Value("${app.jwt.secret:change-this-secret-before-production}")
    private String secret;

    @Value("${app.jwt.expiration-ms:3600000}")
    private long expirationMs;

    public String generateToken(String username) {
        long issuedAt = Instant.now().getEpochSecond();
        long expiresAt = issuedAt + Math.max(1, expirationMs / 1000);

        String headerJson = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";
        String payloadJson = "{\"sub\":\"" + escapeJson(username) + "\",\"iat\":" + issuedAt + ",\"exp\":" + expiresAt + "}";

        String header = encode(headerJson.getBytes(StandardCharsets.UTF_8));
        String payload = encode(payloadJson.getBytes(StandardCharsets.UTF_8));
        String signature = sign(header + "." + payload);

        return header + "." + payload + "." + signature;
    }

    public boolean isTokenValid(String token) {
        if (token == null || token.isBlank()) {
            return false;
        }

        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return false;
        }

        String data = parts[0] + "." + parts[1];
        String expectedSignature = sign(data);
        boolean signatureMatches = MessageDigest.isEqual(
                expectedSignature.getBytes(StandardCharsets.UTF_8),
                parts[2].getBytes(StandardCharsets.UTF_8)
        );

        return signatureMatches && !isExpired(parts[1]);
    }

    private boolean isExpired(String payloadPart) {
        try {
            String payloadJson = new String(URL_DECODER.decode(payloadPart), StandardCharsets.UTF_8);
            Matcher matcher = Pattern.compile("\"exp\"\\s*:\\s*(\\d+)").matcher(payloadJson);
            if (!matcher.find()) {
                return true;
            }
            long expiresAt = Long.parseLong(matcher.group(1));
            return Instant.now().getEpochSecond() >= expiresAt;
        } catch (Exception ex) {
            return true;
        }
    }

    private String sign(String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGORITHM));
            byte[] signature = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return encode(signature);
        } catch (Exception ex) {
            throw new IllegalStateException("Unable to sign JWT token", ex);
        }
    }

    private String encode(byte[] value) {
        return URL_ENCODER.encodeToString(value);
    }

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
