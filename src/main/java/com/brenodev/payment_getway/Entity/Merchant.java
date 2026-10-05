package com.brenodev.payment_getway.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.SecureRandom;
import java.util.HexFormat;

@Entity
@Table(name = "Merchants")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(name = "webhook_url")
    private String webhookUrl;

    @Column(name = "webhook_secret", nullable = false)
    private String webhookSecret;


    public Merchant(Long id, String name) {
        this.id = id;
        this.name = name;
    }


    @PrePersist
    private void generateWebhookSecret() {

        if (this.webhookSecret == null) {

            byte[] bytes = new byte[32];

            new SecureRandom().nextBytes(bytes);

            this.webhookSecret =
                    HexFormat.of().formatHex(bytes);
        }
    }
}

