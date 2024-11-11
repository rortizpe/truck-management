package com.raul.truckmanagement.application.usecase.impl;

import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import com.raul.truckmanagement.application.usecase.port.GetAllTrucksUseCasePort;
import com.raul.truckmanagement.domain.model.Truck;
import com.raul.truckmanagement.domain.repository.TruckRepository;

@Service
@AllArgsConstructor
public class GetAllTrucksUseCaseImpl implements GetAllTrucksUseCasePort {

  private final TruckRepository truckRepository;

  @Override
  public Flux<Truck> execute() {
    return truckRepository.findAll();
  }
}
