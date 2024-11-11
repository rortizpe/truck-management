package com.raul.truckmanagement.application.usecase.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
import com.raul.truckmanagement.application.usecase.command.LoadTruckCommand;
import com.raul.truckmanagement.application.usecase.port.LoadTruckUseCasePort;
import com.raul.truckmanagement.domain.model.Load;
import com.raul.truckmanagement.domain.model.Truck;
import com.raul.truckmanagement.domain.model.TruckStatus;
import com.raul.truckmanagement.domain.repository.LoadRepository;
import com.raul.truckmanagement.domain.repository.TruckRepository;
import com.raul.truckmanagement.infrastructure.service.NotificationService;

@Service
@AllArgsConstructor
public class LoadTruckUseCaseImpl implements LoadTruckUseCasePort {

  private final TruckRepository truckRepository;
  private final LoadRepository loadRepository;
  private final NotificationService notificationService;

  @Override
  public Mono<Load> execute(LoadTruckCommand loadTruckCommand) {
    return truckRepository.findById(loadTruckCommand.getTruckId())
      .flatMap(truck -> validateAndLoadTruck(truck).thenReturn(truck))
      .flatMap(truck -> updateTruckLoad(truck, loadTruckCommand.getLoad()))
      .flatMap(savedLoad -> notifyLoadEvent(loadTruckCommand.getTruckId()).thenReturn(savedLoad));
  }

  private Mono<Void> validateAndLoadTruck(Truck truck) {
    if (truck.getStatus() == TruckStatus.LOADED) {
      return Mono.error(new IllegalStateException("Truck is already loaded"));
    }
    return Mono.empty();
  }

  private Mono<Load> updateTruckLoad(Truck truck, Load load) {
    double newLoadVolume = truck.getCurrentLoad() + load.getVolume();
    if (newLoadVolume > truck.getCapacityLimit()) {
      return Mono.error(new IllegalArgumentException("Load exceeds truck capacity"));
    }
    truck.setCurrentLoad(newLoadVolume);
    truck.setStatus(TruckStatus.LOADED);
    load.setTruckId(truck.getId());

    return truckRepository.save(truck).then(loadRepository.save(load));
  }

  private Mono<Void> notifyLoadEvent(UUID truckId) {
    return notificationService.sendTruckEvent(truckId, TruckStatus.LOADED,
      "Truck loaded successfully");
  }
}