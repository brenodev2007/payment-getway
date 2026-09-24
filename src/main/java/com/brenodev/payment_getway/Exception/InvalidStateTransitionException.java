package com.brenodev.payment_getway.Exception;

import com.brenodev.payment_getway.Enums.TransactionStatus;

public class InvalidStateTransitionException extends RuntimeException {
    public InvalidStateTransitionException(TransactionStatus from, TransactionStatus to) {
        super("Transição inválida: " + from + " -> " + to);
    }
}
