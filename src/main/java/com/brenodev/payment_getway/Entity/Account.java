package com.brenodev.payment_getway.Entity;


import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Table(name = "Accounts")
@Getter
public class Account {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal balance;

    @Column(name = "per_transaction_limit")
    private BigDecimal perTransactionLimit;

    @Column(name = "daily_limit")
    private BigDecimal dailyLimit;

    public void debit(BigDecimal amount) {
        if (balance.compareTo(amount) < 0) throw new IllegalStateException("Saldo insuficiente");
        balance = balance.subtract(amount);
    }

    public void credit(BigDecimal amount) {
        balance = balance.add(amount);
    }
}