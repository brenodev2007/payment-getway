package com.brenodev.payment_getway.Config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "webhook.outbox")
@Getter
@Setter
public class OutboxProperties {

    private long pollingInterval = 5000;
    private int batchSize = 50;
    private int maxAttempts = 10;
    private long initialDelay = 30;
    private long maxDelay = 3600;
}