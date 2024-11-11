package com.raul.truckmanagement.infrastructure.service;

import com.raul.truckmanagement.domain.model.TruckEvent;
import com.raul.truckmanagement.domain.model.TruckStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

  @Mock
  private ReactiveKafkaProducerTemplate<String, TruckEvent> kafkaProducerTemplate;

  @InjectMocks
  private NotificationService notificationService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void sendTruckEvent_success() {
    UUID truckId = UUID.randomUUID();
    TruckStatus status = TruckStatus.LOADED;
    String description = "Truck loaded successfully";

    when(kafkaProducerTemplate.send(anyString(), anyString(), any(TruckEvent.class)))
      .thenReturn(Mono.empty());

    Mono<Void> result = notificationService.sendTruckEvent(truckId, status, description);

    StepVerifier.create(result)
      .verifyComplete();

    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<TruckEvent> eventCaptor = ArgumentCaptor.forClass(TruckEvent.class);

    verify(kafkaProducerTemplate).send(topicCaptor.capture(), keyCaptor.capture(), eventCaptor.capture());

    assertEquals("truck-events", topicCaptor.getValue());
    assertEquals(truckId.toString(), keyCaptor.getValue());
    assertEquals(truckId, eventCaptor.getValue().getTruckId());
    assertEquals(status.name(), eventCaptor.getValue().getStatus());
    assertEquals(description, eventCaptor.getValue().getDescription());
  }

  @Test
  void sendTruckEvent_failure() {
    UUID truckId = UUID.randomUUID();
    TruckStatus status = TruckStatus.LOADED;
    String description = "Truck loaded successfully";

    when(kafkaProducerTemplate.send(anyString(), anyString(), any(TruckEvent.class)))
      .thenReturn(Mono.error(new RuntimeException("Kafka send error")));

    Mono<Void> result = notificationService.sendTruckEvent(truckId, status, description);

    StepVerifier.create(result)
      .expectError(RuntimeException.class)
      .verify();

    ArgumentCaptor<TruckEvent> eventCaptor = ArgumentCaptor.forClass(TruckEvent.class);
    verify(kafkaProducerTemplate).send(anyString(), anyString(), eventCaptor.capture());
  }
}