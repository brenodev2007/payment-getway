package com.brenodev.payment_getway.Enums;


import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public enum TransactionStatus {
    PENDING, PROCESSING, APPROVED, DECLINED, REFUNDED;


    private static final Map<TransactionStatus, Set<TransactionStatus>> ALLOWED =
            new EnumMap<>(TransactionStatus.class);


    static{
        ALLOWED.put(PENDING, ALLOWED.keySet());
        ALLOWED.put(PROCESSING, ALLOWED.keySet());
        ALLOWED.put(APPROVED, ALLOWED.keySet());
        ALLOWED.put(DECLINED, ALLOWED.keySet());
        ALLOWED.put(REFUNDED, ALLOWED.keySet());
    }


    public boolean canTransitionTo(TransactionStatus status){
        return ALLOWED.get(status).contains(this);
    }
}
