package com.raul.truckmanagement.infrastructure.service;

import com.raul.truckmanagement.domain.model.TruckEvent;
import com.raul.truckmanagement.domain.model.TruckStatus;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class NotificationService {

  private final ReactiveKafkaProducerTemplate<String, TruckEvent> kafkaProducerTemplate;

  public NotificationService(ReactiveKafkaProducerTemplate<String, TruckEvent> kafkaProducerTemplate) {
    this.kafkaProducerTemplate = kafkaProducerTemplate;
  }

  public Mono<Void> sendTruckEvent(UUID truckId, TruckStatus status, String description) {
    TruckEvent truckEvent = TruckEvent.builder()
      .truckId(truckId)
      .status(status.name())
      .description(description)
      .build();

    return kafkaProducerTemplate.send("truck-events", truckId.toString(), truckEvent)
      .doOnSuccess(result -> logEvent(truckEvent))
      .then();
  }

  private void logEvent(TruckEvent truckEvent) {
    System.out.println("Event sent: " + truckEvent);
  }
}