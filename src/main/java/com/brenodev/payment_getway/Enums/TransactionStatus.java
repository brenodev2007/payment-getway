package com.brenodev.payment_getway.Enums;


import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

public enum TransactionStatus {
    PENDING, PROCESSING, APPROVED, DECLINED, REFUNDED;

    private static final Map<TransactionStatus, Set<TransactionStatus>> ALLOWED =
            new EnumMap<>(TransactionStatus.class);

    //Mapa do que pode transicionar entre si

    static {
        ALLOWED.put(PENDING,    EnumSet.of(PROCESSING));
        ALLOWED.put(PROCESSING, EnumSet.of(APPROVED, DECLINED));
        ALLOWED.put(APPROVED,   EnumSet.of(REFUNDED));
        ALLOWED.put(DECLINED,   EnumSet.noneOf(TransactionStatus.class));
        ALLOWED.put(REFUNDED,   EnumSet.noneOf(TransactionStatus.class));
    }

    public boolean canTransitionTo(TransactionStatus target) {
        return ALLOWED.get(this).contains(target);
    }
}
