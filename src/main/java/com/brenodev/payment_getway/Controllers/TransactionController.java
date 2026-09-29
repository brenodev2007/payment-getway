package com.brenodev.payment_getway.Controllers;

import com.brenodev.payment_getway.DTOs.TransactionDTO;
import com.brenodev.payment_getway.Entity.Transaction;
import com.brenodev.payment_getway.Services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;


    @PostMapping
    public ResponseEntity<Transaction> createPayment(
            @RequestHeader("Idempotency-Key") String idempotencyKey,
            @RequestBody TransactionDTO request
    ) {
        Transaction transaction =
                transactionService.create(request, idempotencyKey);

        return ResponseEntity.ok(transaction);
    }



    @PostMapping("/{transactionId}/refund")
    public Transaction refund(@PathVariable Long transactionId) {
        Transaction transaction = transactionService.refund(transactionId);

        return transaction;
    }
}
