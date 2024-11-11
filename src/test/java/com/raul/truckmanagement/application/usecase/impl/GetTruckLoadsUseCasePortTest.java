package com.raul.truckmanagement.application.usecase.impl;

import com.raul.truckmanagement.domain.model.Load;
import com.raul.truckmanagement.domain.repository.LoadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.when;

class GetTruckLoadsUseCasePortTest {

  @Mock
  private LoadRepository loadRepository;

  @InjectMocks
  private GetTruckLoadsUseCaseImpl getTruckLoadsUseCase;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void givenTruckWithLoads_whenExecute_thenReturnsLoadList() {
    UUID truckId = UUID.randomUUID();
    Load load1 = new Load(UUID.randomUUID(), truckId, 200.0, "Materiales de construcción", LocalDateTime.now(), null);
    Load load2 = new Load(UUID.randomUUID(), truckId, 150.0, "Materiales de construcción adicionales", LocalDateTime.now(), null);

    when(loadRepository.findByTruckId(truckId)).thenReturn(Flux.just(load1, load2));

    Flux<Load> result = getTruckLoadsUseCase.execute(truckId);

    StepVerifier.create(result)
      .expectNext(load1)
      .expectNext(load2)
      .verifyComplete();
  }

  @Test
  void givenTruckWithoutLoads_whenExecute_thenReturnsEmptyFlux() {
    UUID truckId = UUID.randomUUID();

    when(loadRepository.findByTruckId(truckId)).thenReturn(Flux.empty());

    Flux<Load> result = getTruckLoadsUseCase.execute(truckId);

    StepVerifier.create(result)
      .verifyComplete();
  }
}