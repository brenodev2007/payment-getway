package com.brenodev.payment_getway.Controllers;

import com.brenodev.payment_getway.DTOs.TransactionStatusHistoryDTO;
import com.brenodev.payment_getway.Services.TransactionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TransactionHistoryController {

    private final TransactionHistoryService transactionHistoryService;

    @GetMapping("/{id}/history")
    public ResponseEntity<List<TransactionStatusHistoryDTO>> getHistory(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                transactionHistoryService.findByTransactionId(id)
        );
    }

}
