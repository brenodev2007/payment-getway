
package com.brenodev.payment_getway.Services;

import com.brenodev.payment_getway.DTOs.WebhookMessageDTO;
import com.brenodev.payment_getway.Entity.OutboxEvent;
import com.brenodev.payment_getway.Config.RabbitMQConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RabbitMQPublisherService {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public void publish(OutboxEvent event) {
        try {

            WebhookMessageDTO webhookMessage = new WebhookMessageDTO(
                    event.getEventId(),
                    event.getEventType(),
                    event.getAggregateId(),
                    event.getPayload()
            );

            byte[] body = objectMapper.writeValueAsBytes(webhookMessage);

            MessageProperties properties = new MessageProperties();
            properties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
            properties.setMessageId(event.getEventId().toString());
            properties.setHeader("eventType", event.getEventType());

            Message message = new Message(body, properties);

            CorrelationData correlationData =
                    new CorrelationData(event.getEventId().toString());

            rabbitTemplate.send(
                    RabbitMQConfig.WEBHOOK_EXCHANGE,
                    RabbitMQConfig.WEBHOOK_ROUTING_KEY,
                    message,
                    correlationData
            );

            var confirm = correlationData.getFuture()
                    .get(10, TimeUnit.SECONDS);

            if (!confirm.ack()) {
                throw new IllegalStateException(
                        "RabbitMQ rejeitou o evento: " + confirm.reason()
                );
            }



        } catch (JsonProcessingException e) {
            throw new IllegalStateException(
                    "Não foi possível serializar o evento da Outbox.", e
            );
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Falha ao publicar evento no RabbitMQ: "
                            + event.getEventId(),
                    e
            );
        }
    }
}
