package com.brenodev.payment_getway.Services;

import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;

@Service
public class WebhookSignatureService {

    private static final String HMAC_ALGORITHM = "HmacSHA256";

    public String generateSignature(
            String payload,
            String secret
    ) {

        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);

            SecretKeySpec key = new SecretKeySpec(
                    secret.getBytes(StandardCharsets.UTF_8),
                    HMAC_ALGORITHM
            );

            mac.init(key);

            byte[] hash = mac.doFinal(
                    payload.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Não foi possível gerar assinatura do webhook",
                    e
            );
        }
    }

    public boolean isValid(
            String payload,
            String secret,
            String receivedSignature
    ) {

        String expectedSignature =
                generateSignature(payload, secret);

        return MessageDigest.isEqual(
                expectedSignature.getBytes(StandardCharsets.UTF_8),
                receivedSignature.getBytes(StandardCharsets.UTF_8)
        );
    }
}