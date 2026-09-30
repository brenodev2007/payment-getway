package com.brenodev.payment_getway.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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


    public Merchant(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

