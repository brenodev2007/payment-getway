package com.brenodev.payment_getway.Services;

import com.brenodev.payment_getway.DTOs.TransactionWebhookDTO;
import com.brenodev.payment_getway.Entity.OutboxEvent;
import com.brenodev.payment_getway.Enums.OutboxStatus;
import com.brenodev.payment_getway.Repositories.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.json.JsonParseException;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.core.JacksonException;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutboxService {

    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;


    public void createEvent(
            String eventType,
            Long transactionId,
            String destinationUrl,
            TransactionWebhookDTO payload
    ){
        if(destinationUrl == null || destinationUrl.isEmpty()){
            return;
        }

        try{
            OutboxEvent event = new OutboxEvent();
            event.setEventId(UUID.randomUUID());
            event.setEventType(eventType);
            event.setAggregateType("TRANSACTION");
            event.setAggregateId(transactionId);
            event.setDestinationUrl(destinationUrl);
            event.setPayload(objectMapper.writeValueAsString(payload));
            event.setStatus(OutboxStatus.PENDING);
            event.setAttempts(0);
            event.setNextAttemptAt(Instant.now());

            outboxEventRepository.save(event);
        } catch(JacksonException  e){
            throw new IllegalStateException(
                    "Erro ao serializar evento de webhook",
                    e
            );
        }
    }
}
