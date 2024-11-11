package com.raul.truckmanagement.application.usecase.impl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
import com.raul.truckmanagement.application.usecase.port.UnloadTruckUseCasePort;
import com.raul.truckmanagement.domain.model.Truck;
import com.raul.truckmanagement.domain.model.TruckStatus;
import com.raul.truckmanagement.domain.repository.LoadRepository;
import com.raul.truckmanagement.domain.repository.TruckRepository;
import com.raul.truckmanagement.infrastructure.service.NotificationService;

@Service
@AllArgsConstructor
public class UnloadTruckUseCaseImpl implements UnloadTruckUseCasePort {

  private final TruckRepository truckRepository;
  private final LoadRepository loadRepository;
  private final NotificationService notificationService;

  @Override
  public Mono<Void> execute(UUID truckId) {
    return truckRepository.findById(truckId)
      .flatMap(this::validateUnloadTruck)
      .flatMap(this::saveTruckAsUnloaded)
      .flatMap(this::updateLoad)
      .flatMap(this::notifyUnloadEvent);
  }

  private Mono<Truck> validateUnloadTruck(Truck truck) {
    if (truck.getStatus() == TruckStatus.AVAILABLE) {
      return Mono.error(new IllegalStateException("Truck is not loaded"));
    }
    return Mono.just(truck);
  }

  private Mono<Truck> saveTruckAsUnloaded(Truck truck) {
    truck.setCurrentLoad(0.0);
    truck.setStatus(TruckStatus.UNLOADED);
    return truckRepository.save(truck);
  }

  private Mono<Truck> updateLoad(Truck truck) {
    return loadRepository.findFirstByTruckIdOrderByLoadTimestampDesc(truck.getId())
      .doOnNext(load -> load.setUnloadTimestamp(LocalDateTime.now()))
      .flatMap(loadRepository::save)
      .thenReturn(truck);
  }

  private Mono<Void> notifyUnloadEvent(Truck truck) {
    return notificationService.sendTruckEvent(truck.getId(), TruckStatus.UNLOADED,
      "Truck unloaded successfully");
  }
}