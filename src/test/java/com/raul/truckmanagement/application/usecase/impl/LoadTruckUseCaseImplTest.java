package com.raul.truckmanagement.application.usecase.impl;

import com.raul.truckmanagement.application.usecase.command.LoadTruckCommand;
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

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoadTruckUseCaseImplTest {

  @Mock
  private TruckRepository truckRepository;

  @Mock
  private LoadRepository loadRepository;

  @Mock
  private NotificationService notificationService;

  @InjectMocks
  private LoadTruckUseCaseImpl loadTruckUseCase;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void givenValidTruckAndLoadCommand_whenExecute_thenLoadIsAdded() {
    UUID truckId = UUID.randomUUID();
    Truck truck = new Truck(truckId, "ABC123", "ModelX", 1000.0, 0.0, TruckStatus.AVAILABLE);
    Load load = new Load(UUID.randomUUID(), truckId, 200.0, "Materials", null, null);
    LoadTruckCommand command = new LoadTruckCommand(truckId, load);

    when(truckRepository.findById(truckId)).thenReturn(Mono.just(truck));
    when(truckRepository.save(truck)).thenReturn(Mono.just(truck));
    when(loadRepository.save(load)).thenReturn(Mono.just(load));
    when(notificationService.sendTruckEvent(truckId, TruckStatus.LOADED, "Truck loaded successfully")).thenReturn(Mono.empty());

    Mono<Load> result = loadTruckUseCase.execute(command);

    StepVerifier.create(result)
      .expectNextMatches(savedLoad -> savedLoad.getVolume().equals(200.0))
      .verifyComplete();

    verify(truckRepository).findById(truckId);
    verify(loadRepository).save(load);
    verify(notificationService).sendTruckEvent(truckId, TruckStatus.LOADED, "Truck loaded successfully");
  }

  @Test
  void givenTruckAlreadyLoaded_whenExecute_thenThrowsIllegalStateException() {
    UUID truckId = UUID.randomUUID();
    Truck truck = new Truck(truckId, "DEF456", "ModelY", 1000.0, 0.0, TruckStatus.LOADED);
    Load load = new Load(UUID.randomUUID(), truckId, 200.0, "Materials", null, null);
    LoadTruckCommand command = new LoadTruckCommand(truckId, load);

    when(truckRepository.findById(truckId)).thenReturn(Mono.just(truck));

    Mono<Load> result = loadTruckUseCase.execute(command);

    StepVerifier.create(result)
      .expectErrorMatches(throwable -> throwable instanceof IllegalStateException && throwable.getMessage().equals("Truck is already loaded"))
      .verify();

    verify(truckRepository).findById(truckId);
    verify(loadRepository, never()).save(any());
    verify(notificationService, never()).sendTruckEvent(any(), any(), any());
  }

  @Test
  void givenLoadExceedsCapacity_whenExecute_thenThrowsIllegalArgumentException() {
    UUID truckId = UUID.randomUUID();
    Truck truck = new Truck(truckId, "XYZ789", "ModelZ", 1000.0, 900.0, TruckStatus.AVAILABLE);
    Load load = new Load(UUID.randomUUID(), truckId, 200.0, "Excessive load", null, null);
    LoadTruckCommand command = new LoadTruckCommand(truckId, load);

    when(truckRepository.findById(truckId)).thenReturn(Mono.just(truck));

    Mono<Load> result = loadTruckUseCase.execute(command);

    StepVerifier.create(result)
      .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException && throwable.getMessage().equals("Load exceeds truck capacity"))
      .verify();

    verify(truckRepository).findById(truckId);
    verify(loadRepository, never()).save(any());
    verify(notificationService, never()).sendTruckEvent(any(), any(), any());
  }
}