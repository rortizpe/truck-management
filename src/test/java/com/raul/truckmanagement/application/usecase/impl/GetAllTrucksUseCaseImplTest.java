package com.raul.truckmanagement.application.usecase.impl;

import com.raul.truckmanagement.domain.model.Truck;
import com.raul.truckmanagement.domain.model.TruckStatus;
import com.raul.truckmanagement.domain.repository.TruckRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.Mockito.when;

class GetAllTrucksUseCaseImplTest {

  @Mock
  private TruckRepository truckRepository;

  @InjectMocks
  private GetAllTrucksUseCaseImpl getAllTrucksUseCase;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void givenExistingTrucks_whenExecute_thenReturnsTruckList() {
    Truck truck1 = new Truck(UUID.randomUUID(), "ABC123", "ModelX", 1000.0, 200.0, TruckStatus.AVAILABLE);
    Truck truck2 = new Truck(UUID.randomUUID(), "DEF456", "ModelY", 1200.0, 300.0, TruckStatus.LOADED);

    when(truckRepository.findAll()).thenReturn(Flux.just(truck1, truck2));

    Flux<Truck> result = getAllTrucksUseCase.execute();

    StepVerifier.create(result)
      .expectNext(truck1)
      .expectNext(truck2)
      .verifyComplete();
  }

  @Test
  void givenNoTrucks_whenExecute_thenReturnsEmptyFlux() {
    when(truckRepository.findAll()).thenReturn(Flux.empty());

    Flux<Truck> result = getAllTrucksUseCase.execute();

    StepVerifier.create(result)
      .verifyComplete();
  }
}