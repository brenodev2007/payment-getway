
package com.brenodev.payment_getway.Config;


import com.brenodev.payment_getway.DTOs.WebhookMessageDTO;
import com.brenodev.payment_getway.Services.WebhookDeliveryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebhookConsumer {

    private final ObjectMapper objectMapper;
    private final WebhookDeliveryService deliveryService;

    @RabbitListener(queues = RabbitMQConfig.WEBHOOK_QUEUE)
    public void consume(Message message) throws Exception {

        WebhookMessageDTO webhookMessage = objectMapper.readValue(
                message.getBody(),
                WebhookMessageDTO.class
        );

        log.info(
                "Mensagem recebida do RabbitMQ. eventId={}, type={}",
                webhookMessage.eventId(),
                webhookMessage.eventType()
        );
        deliveryService.deliver(webhookMessage);
    }
}
