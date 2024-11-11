package com.raul.truckmanagement.application.usecase.impl;

import com.raul.truckmanagement.domain.model.Load;
import com.raul.truckmanagement.domain.model.Truck;
import com.raul.truckmanagement.domain.model.TruckStatus;
import com.raul.truckmanagement.domain.repository.LoadRepository;
import com.raul.truckmanagement.domain.repository.TruckRepository;
import com.raul.truckmanagement.infrastructure.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UnloadTruckUseCaseImplTest {

  @Mock
  private TruckRepository truckRepository;

  @Mock
  private LoadRepository loadRepository;

  @Mock
  private NotificationService notificationService;

  @InjectMocks
  private UnloadTruckUseCaseImpl unloadTruckUseCase;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void givenLoadedTruck_whenExecute_thenUnloadsTruckSuccessfully() {
    UUID truckId = UUID.randomUUID();
    Truck truck = new Truck(truckId, "ABC123", "ModelX", 1000.0, 500.0, TruckStatus.LOADED);
    Load latestLoad = new Load(UUID.randomUUID(), truckId, 500.0, "Cargo", LocalDateTime.now(), null);

    when(truckRepository.findById(truckId)).thenReturn(Mono.just(truck));
    when(truckRepository.save(any(Truck.class))).thenReturn(Mono.just(truck));
    when(loadRepository.findFirstByTruckIdOrderByLoadTimestampDesc(truckId)).thenReturn(Mono.just(latestLoad));
    when(loadRepository.save(any(Load.class))).thenReturn(Mono.just(latestLoad));
    when(notificationService.sendTruckEvent(truckId, TruckStatus.UNLOADED, "Truck unloaded successfully")).thenReturn(Mono.empty());

    Mono<Void> result = unloadTruckUseCase.execute(truckId);

    StepVerifier.create(result)
      .verifyComplete();

    verify(truckRepository).findById(truckId);
    verify(truckRepository).save(any(Truck.class));
    verify(loadRepository).findFirstByTruckIdOrderByLoadTimestampDesc(truckId);
    verify(loadRepository).save(any(Load.class));
    verify(notificationService).sendTruckEvent(truckId, TruckStatus.UNLOADED, "Truck unloaded successfully");
  }
  @Test
  void givenAvailableTruck_whenExecute_thenThrowsIllegalStateException() {
    UUID truckId = UUID.randomUUID();
    Truck truck = new Truck(truckId, "ABC123", "ModelX", 1000.0, 0.0, TruckStatus.AVAILABLE);

    when(truckRepository.findById(truckId)).thenReturn(Mono.just(truck));
    when(loadRepository.findFirstByTruckIdOrderByLoadTimestampDesc(truckId)).thenReturn(Mono.empty());
    when(notificationService.sendTruckEvent(truckId, TruckStatus.UNLOADED, "Truck unloaded successfully")).thenReturn(Mono.empty());

    Mono<Void> result = unloadTruckUseCase.execute(truckId);

    StepVerifier.create(result)
      .expectErrorMatches(throwable -> throwable instanceof IllegalStateException &&
        throwable.getMessage().equals("Truck is not loaded"))
      .verify();

    verify(truckRepository).findById(truckId);
    verify(loadRepository, never()).save(any());
    verify(notificationService, never()).sendTruckEvent(any(), any(), any());
  }
}