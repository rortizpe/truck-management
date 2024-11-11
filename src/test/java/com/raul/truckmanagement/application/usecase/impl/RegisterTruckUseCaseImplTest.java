package com.raul.truckmanagement.application.usecase.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.raul.truckmanagement.domain.model.Truck;
import com.raul.truckmanagement.domain.model.TruckStatus;
import com.raul.truckmanagement.domain.repository.TruckRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class RegisterTruckUseCaseImplTest {

  @Mock
  private TruckRepository truckRepository;

  @InjectMocks
  private RegisterTruckUseCaseImpl registerTruckUseCase;

  private Truck truck;

  @BeforeEach
  void setUp() {
    truck = new Truck(UUID.randomUUID(), "ABC-123", "Model X", 10000.0, 0.0, TruckStatus.AVAILABLE);
  }

  @Test
  void givenNewTruck_whenExecute_thenReturnSavedTruckWithStatusAvailable() {
    when(truckRepository.save(any(Truck.class))).thenReturn(Mono.just(truck));

    StepVerifier.create(registerTruckUseCase.execute(truck))
      .assertNext(savedTruck -> {
        assertEquals(TruckStatus.AVAILABLE, savedTruck.getStatus());
        assertEquals("ABC-123", savedTruck.getLicensePlate());
      })
      .verifyComplete();
  }

  @Test
  void givenTruckId_whenGetTruckById_thenReturnTruck() {
    when(truckRepository.findById(any(UUID.class))).thenReturn(Mono.just(truck));

    StepVerifier.create(registerTruckUseCase.getTruckById(truck.getId()))
      .assertNext(foundTruck -> {
        assertEquals(truck.getId(), foundTruck.getId());
        assertEquals("Model X", foundTruck.getModel());
      })
      .verifyComplete();
  }

  @Test
  void givenInvalidTruckId_whenGetTruckById_thenReturnEmptyMono() {
    when(truckRepository.findById(any(UUID.class))).thenReturn(Mono.empty());

    StepVerifier.create(registerTruckUseCase.getTruckById(UUID.randomUUID()))
      .expectNextCount(0)
      .verifyComplete();
  }
}