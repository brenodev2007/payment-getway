package com.brenodev.payment_getway.Entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "Accounts")
@Getter
@Setter
public class Account {
    private static final BigDecimal DEFAULT_PER_TRANSACTION_LIMIT = new BigDecimal("5000.00");
    private static final BigDecimal DEFAULT_DAILY_LIMIT = new BigDecimal("10000.00");

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


    public Account(BigDecimal initialBalance, BigDecimal perTransactionLimit, BigDecimal dailyLimit) {
        this.balance = initialBalance != null ? initialBalance : BigDecimal.ZERO;
        this.perTransactionLimit = perTransactionLimit != null ? perTransactionLimit : DEFAULT_PER_TRANSACTION_LIMIT;
        this.dailyLimit = dailyLimit != null ? dailyLimit : DEFAULT_DAILY_LIMIT;
    }
}