package com.raul.truckmanagement.application.usecase.impl;

import com.raul.truckmanagement.domain.model.Load;
import com.raul.truckmanagement.domain.repository.LoadRepository;
import com.raul.truckmanagement.domain.repository.TruckRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.*;

class DeleteTruckUseCaseImplTest {

  @Mock
  private TruckRepository truckRepository;

  @Mock
  private LoadRepository loadRepository;

  @InjectMocks
  private DeleteTruckUseCaseImpl deleteTruckUseCase;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void givenTruckWithNoLoads_whenExecute_thenDeletesTruck() {
    UUID truckId = UUID.randomUUID();

    when(loadRepository.findByTruckId(truckId)).thenReturn(Flux.empty());
    when(truckRepository.deleteById(truckId)).thenReturn(Mono.empty());

    Mono<Void> result = deleteTruckUseCase.execute(truckId);

    StepVerifier.create(result)
      .verifyComplete();

    verify(loadRepository).findByTruckId(truckId);
    verify(truckRepository).deleteById(truckId);
  }

  @Test
  void givenTruckWithLoads_whenExecute_thenThrowsIllegalStateException() {
    UUID truckId = UUID.randomUUID();
    Load load = new Load(
      UUID.randomUUID(),
      truckId,
      200.0,
      "Carga de prueba",
      LocalDateTime.now(),
      null
    );

    when(loadRepository.findByTruckId(truckId)).thenReturn(Flux.just(load));

    Mono<Void> result = deleteTruckUseCase.execute(truckId);

    StepVerifier.create(result)
      .expectErrorMatches(throwable -> throwable instanceof IllegalStateException &&
        throwable.getMessage().equals("No se puede eliminar un camión con cargas activas"))
      .verify();

    verify(loadRepository).findByTruckId(truckId);
    verify(truckRepository, never()).deleteById((UUID) any());
  }
}