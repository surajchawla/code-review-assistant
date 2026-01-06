package com.codereview.webhook.services;

import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
public class SignatureValidator implements ISignatureValidator {

    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final String PREFIX = "sha256=";

    @Override
    public boolean validate(String payload, String signatureHeader, String secret) {
        try {
            if (!signatureHeader.startsWith(PREFIX)) {
                return false;
            }

            String expectedSignature = signatureHeader.substring(PREFIX.length());

            Mac mac = Mac.getInstance(HMAC_SHA256);
            SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_SHA256);
            mac.init(keySpec);

            byte[] computedHash = mac.doFinal(payload.getBytes(StandardCharsets.UTF_8));
            String computedSignature = bytesToHex(computedHash);

            return MessageDigest.isEqual(
                    computedSignature.getBytes(StandardCharsets.UTF_8),
                    expectedSignature.getBytes(StandardCharsets.UTF_8)
            );

        } catch (Exception e) {
            return false;
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}