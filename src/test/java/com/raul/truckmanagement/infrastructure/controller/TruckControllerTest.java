package com.raul.truckmanagement.infrastructure.controller;

import com.raul.truckmanagement.application.dto.request.LoadRequest;
import com.raul.truckmanagement.application.dto.request.TruckRequest;
import com.raul.truckmanagement.application.dto.response.LoadResponse;
import com.raul.truckmanagement.application.dto.response.TruckResponse;
import com.raul.truckmanagement.application.mapper.LoadMapper;
import com.raul.truckmanagement.application.mapper.TruckMapper;
import com.raul.truckmanagement.application.usecase.command.LoadTruckCommand;
import com.raul.truckmanagement.application.usecase.impl.*;
import com.raul.truckmanagement.domain.model.Load;
import com.raul.truckmanagement.domain.model.Truck;
import com.raul.truckmanagement.domain.model.TruckStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class TruckControllerTest {

  @Mock
  private RegisterTruckUseCaseImpl registerTruckUseCaseImpl;
  @Mock
  private LoadTruckUseCaseImpl loadTruckUseCaseImpl;
  @Mock
  private UnloadTruckUseCaseImpl unloadTruckUseCaseImpl;
  @Mock
  private GetTruckLoadsUseCaseImpl getTruckLoadsUseCaseImpl;
  @Mock
  private DeleteTruckUseCaseImpl deleteTruckUseCaseImpl;
  @Mock
  private GetAllTrucksUseCaseImpl getAllTrucksUseCaseImpl;

  @InjectMocks
  private TruckController truckController;

  private WebTestClient webTestClient;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    webTestClient = WebTestClient.bindToController(truckController).build();
  }

  @Test
  void registerTruck() {
    TruckRequest truckRequest = new TruckRequest();
    truckRequest.setLicensePlate("ABC123");
    truckRequest.setModel("ModelX");
    truckRequest.setCapacityLimit(1000.0);
    truckRequest.setCurrentLoad(0.0);
    truckRequest.setStatus("AVAILABLE");

    Truck truck = TruckMapper.INSTANCE.toEntity(truckRequest);

    when(registerTruckUseCaseImpl.execute(any(Truck.class))).thenReturn(Mono.just(truck));

    webTestClient.post()
      .uri("/api/trucks")
      .bodyValue(truckRequest)
      .exchange()
      .expectStatus().isOk()
      .expectBody(TruckResponse.class);
  }

  @Test
  void getAllTrucks() {
    Truck truck = new Truck(UUID.randomUUID(), "ABC123", "ModelX", 1000.0, 0.0, TruckStatus.AVAILABLE);

    when(getAllTrucksUseCaseImpl.execute()).thenReturn(Flux.just(truck));

    webTestClient.get()
      .uri("/api/trucks")
      .exchange()
      .expectStatus().isOk()
      .expectBodyList(TruckResponse.class)
      .hasSize(1);
  }

  @Test
  void getTruckById() {
    UUID truckId = UUID.randomUUID();
    Truck truck = new Truck(truckId, "ABC123", "ModelX", 1000.0, 0.0, TruckStatus.AVAILABLE);

    when(registerTruckUseCaseImpl.getTruckById(truckId)).thenReturn(Mono.just(truck));

    webTestClient.get()
      .uri("/api/trucks/{id}", truckId)
      .exchange()
      .expectStatus().isOk()
      .expectBody(TruckResponse.class)
      .returnResult();
  }

  @Test
  void loadTruck() {
    UUID truckId = UUID.randomUUID();
    LoadRequest loadRequest = new LoadRequest();
    loadRequest.setVolume(200.0);
    loadRequest.setDescription("Cargo");

    Load load = LoadMapper.INSTANCE.toEntity(loadRequest);

    when(loadTruckUseCaseImpl.execute(any())).thenReturn(Mono.just(load));

    webTestClient.post()
      .uri("/api/trucks/{id}/load", truckId)
      .bodyValue(loadRequest)
      .exchange()
      .expectStatus().isOk().expectBody();
  }

  @Test
  void unloadTruck() {
    UUID truckId = UUID.randomUUID();

    when(unloadTruckUseCaseImpl.execute(truckId)).thenReturn(Mono.empty());

    webTestClient.post()
      .uri("/api/trucks/{id}/unload", truckId)
      .exchange()
      .expectStatus().isOk();
  }

  @Test
  void getTruckLoads() {
    UUID truckId = UUID.randomUUID();
    Load load = new Load(UUID.randomUUID(), truckId, 200.0, "Cargo", LocalDateTime.now(), null);

    when(getTruckLoadsUseCaseImpl.execute(truckId)).thenReturn(Flux.just(load));

    webTestClient.get()
      .uri("/api/trucks/{id}/loads", truckId)
      .exchange()
      .expectStatus().isOk()
      .expectBodyList(LoadResponse.class);
  }

  @Test
  void deleteTruck() {
    UUID truckId = UUID.randomUUID();

    when(deleteTruckUseCaseImpl.execute(truckId)).thenReturn(Mono.empty());

    webTestClient.delete()
      .uri("/api/trucks/{id}", truckId)
      .exchange()
      .expectStatus().isOk();
  }
}