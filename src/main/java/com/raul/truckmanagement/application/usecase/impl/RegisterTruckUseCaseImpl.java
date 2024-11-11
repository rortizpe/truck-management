package com.raul.truckmanagement.application.usecase.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
import com.raul.truckmanagement.application.usecase.port.RegisterTruckUseCasePort;
import com.raul.truckmanagement.domain.model.Truck;
import com.raul.truckmanagement.domain.model.TruckStatus;
import com.raul.truckmanagement.domain.repository.TruckRepository;

@Service
@AllArgsConstructor
public class RegisterTruckUseCaseImpl implements RegisterTruckUseCasePort {

  private final TruckRepository truckRepository;

  @Override
  public Mono<Truck> execute(Truck truck) {
    truck.setStatus(TruckStatus.AVAILABLE);
    return truckRepository.save(truck);
  }

  @Override
  public Mono<Truck> getTruckById(UUID id) {
    return truckRepository.findById(id);
  }
}