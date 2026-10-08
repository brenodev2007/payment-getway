package com.brenodev.payment_getway.Config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String WEBHOOK_EXCHANGE =
            "payment.webhook.exchange";
    public static final String WEBHOOK_QUEUE =
            "payment.webhook.queue";
    public static final String WEBHOOK_ROUTING_KEY =
            "webhook.event";

    public static final String WEBHOOK_DLX =
            "payment.webhook.dlx";
    public static final String WEBHOOK_DLQ =
            "payment.webhook.dlq";
    public static final String WEBHOOK_DLQ_ROUTING_KEY =
            "webhook.dead";

    @Bean
    public DirectExchange webhookExchange() {
        return new DirectExchange(WEBHOOK_EXCHANGE, true, false);
    }

    @Bean
    public Queue webhookQueue() {
        return QueueBuilder.durable(WEBHOOK_QUEUE)
                .deadLetterExchange(WEBHOOK_DLX)
                .deadLetterRoutingKey(WEBHOOK_DLQ_ROUTING_KEY)
                .build();
    }

    @Bean
    public DirectExchange webhookDeadLetterExchange() {
        return new DirectExchange(WEBHOOK_DLX, true, false);
    }

    @Bean
    public Queue webhookDeadLetterQueue() {
        return QueueBuilder.durable(WEBHOOK_DLQ).build();
    }

    @Bean
    public Binding webhookBinding() {
        return BindingBuilder.bind(webhookQueue())
                .to(webhookExchange())
                .with(WEBHOOK_ROUTING_KEY);
    }

    @Bean
    public Binding webhookDeadLetterBinding() {
        return BindingBuilder.bind(webhookDeadLetterQueue())
                .to(webhookDeadLetterExchange())
                .with(WEBHOOK_DLQ_ROUTING_KEY);
    }
}