package com.brenodev.payment_getway.Controllers;

import com.brenodev.payment_getway.DTOs.FailedOutboxEventDTO;
import com.brenodev.payment_getway.Services.OutboxMonitoringService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/outbox")
@RequiredArgsConstructor
public class OutboxMonitoringController {

    private final OutboxMonitoringService service;

    @GetMapping("/failed")
    public ResponseEntity<List<FailedOutboxEventDTO>> failedEvents() {

        return ResponseEntity.ok(
                service.findFailedEvents()
        );
    }

    @GetMapping("/failed/count")
    public ResponseEntity<Long> failedCount() {

        return ResponseEntity.ok(
                service.countFailedEvents()
        );

    }
}