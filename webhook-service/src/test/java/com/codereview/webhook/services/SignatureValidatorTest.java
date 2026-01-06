package com.codereview.webhook.services;

import org.junit.jupiter.api.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SignatureValidatorTest {
    @Test
    void validSignatureReturnsTrue() {
        ISignatureValidator validator = new SignatureValidator();
        String payload = "{\"test\":true}";
        String secret = "mysecret";
        String signature = "sha256=" + computeHmac(payload, secret);
        assertTrue(validator.validate(payload, signature, secret));
    }

    @Test
    void invalidSignatureReturnsFalse() {
        ISignatureValidator validator = new SignatureValidator();
        String payload = "{\"test\":true}";
        String secret = "mysecret";
        String signature = "sha256=invalidsignature";
        assertFalse(validator.validate(payload, signature, secret));
    }

    @Test
    void missingPrefixReturnsFalse() {
        ISignatureValidator validator = new SignatureValidator();
        String payload = "{\"test\":true}";
        String secret = "mysecret";
        String signature = computeHmac(payload, secret); // No "sha256=" prefix
        assertFalse(validator.validate(payload, signature, secret));
    }

    @Test
    void wrongSecretReturnsFalse() {
        ISignatureValidator validator = new SignatureValidator();
        String payload = "{\"test\":true}";
        String secret = "mysecret";
        String wrongSecret = "notmysecret";
        String signature = "sha256=" + computeHmac(payload, wrongSecret);
        assertFalse(validator.validate(payload, signature, secret));
    }

    @Test
    void nullSignatureReturnsFalse() {
        ISignatureValidator validator = new SignatureValidator();
        String payload = "{\"test\":true}";
        String secret = "mysecret";
        assertFalse(validator.validate(payload, null, secret));
    }

    @Test
    void emptySignatureReturnsFalse() {
        ISignatureValidator validator = new SignatureValidator();
        String payload = "{\"test\":true}";
        String secret = "mysecret";
        assertFalse(validator.validate(payload, "", secret));
    }

    private String computeHmac(String payload, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(keySpec);
            byte[] rawHmac = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : rawHmac) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to compute HMAC", e);
        }
    }
}