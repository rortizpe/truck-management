package com.raul.truckmanagement.infrastructure.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import com.raul.truckmanagement.application.dto.request.LoadRequest;
import com.raul.truckmanagement.application.dto.request.TruckRequest;
import com.raul.truckmanagement.application.dto.response.LoadResponse;
import com.raul.truckmanagement.application.dto.response.TruckResponse;
import com.raul.truckmanagement.application.mapper.LoadMapper;
import com.raul.truckmanagement.application.mapper.TruckMapper;
import com.raul.truckmanagement.application.usecase.command.LoadTruckCommand;
import com.raul.truckmanagement.application.usecase.port.DeleteTruckUseCasePort;
import com.raul.truckmanagement.application.usecase.port.GetAllTrucksUseCasePort;
import com.raul.truckmanagement.application.usecase.port.GetTruckLoadsUseCasePort;
import com.raul.truckmanagement.application.usecase.port.LoadTruckUseCasePort;
import com.raul.truckmanagement.application.usecase.port.RegisterTruckUseCasePort;
import com.raul.truckmanagement.application.usecase.port.UnloadTruckUseCasePort;
import com.raul.truckmanagement.domain.model.Load;
import com.raul.truckmanagement.domain.model.Truck;

@RestController
@AllArgsConstructor
@RequestMapping("/api/trucks")
public class TruckController {
  private final RegisterTruckUseCasePort registerTruckUseCasePort;
  private final LoadTruckUseCasePort loadTruckUseCasePort;
  private final UnloadTruckUseCasePort unloadTruckUseCasePort;
  private final GetTruckLoadsUseCasePort getTruckLoadsUseCasePort;
  private final DeleteTruckUseCasePort deleteTruckUseCasePort;
  private final GetAllTrucksUseCasePort getAllTrucksUseCasePort;

  @Operation(summary = "Registrar un nuevo camión")
  @PostMapping
  public Mono<TruckResponse> registerTruck(@RequestBody TruckRequest truckRequest) {
    Truck truck = TruckMapper.INSTANCE.toEntity(truckRequest);
    return registerTruckUseCasePort.execute(truck).map(TruckMapper.INSTANCE::toResponse);
  }

  @Operation(summary = "Listar todos los camiones")
  @GetMapping
  public Flux<TruckResponse> getAllTrucks() {
    return getAllTrucksUseCasePort.execute().map(TruckMapper.INSTANCE::toResponse);
  }

  @Operation(summary = "Obtener detalles de un camión")
  @GetMapping("/{id}")
  public Mono<TruckResponse> getTruckById(@PathVariable UUID id) {
    return registerTruckUseCasePort.getTruckById(id).map(TruckMapper.INSTANCE::toResponse);
  }

  @Operation(summary = "Añadir carga al camión")
  @PostMapping("/{id}/load")
  public Mono<Void> loadTruck(@PathVariable UUID id, @RequestBody LoadRequest loadRequest) {
    Load load = LoadMapper.INSTANCE.toEntity(loadRequest);
    LoadTruckCommand command = LoadTruckCommand.builder().truckId(id).load(load).build();

    return loadTruckUseCasePort.execute(command).then();
  }

  @Operation(summary = "Descargar el camión y enviar notificación")
  @PostMapping("/{id}/unload")
  public Mono<Void> unloadTruck(@PathVariable UUID id) {
    return unloadTruckUseCasePort.execute(id);
  }

  @Operation(summary = "Consultar historial de cargas del camión")
  @GetMapping("/{id}/loads")
  public Flux<LoadResponse> getTruckLoads(@PathVariable UUID id) {
    return getTruckLoadsUseCasePort.execute(id).map(LoadMapper.INSTANCE::toResponse);
  }

  @Operation(summary = "Eliminar un camión")
  @DeleteMapping("/{id}")
  public Mono<Void> deleteTruck(@PathVariable UUID id) {
    return deleteTruckUseCasePort.execute(id);
  }
}