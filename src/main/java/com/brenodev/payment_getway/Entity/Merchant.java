package com.brenodev.payment_getway.Entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Table(name = "Merchants")
@Getter
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
}

