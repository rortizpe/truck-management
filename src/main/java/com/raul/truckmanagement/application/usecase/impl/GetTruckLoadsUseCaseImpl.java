package com.raul.truckmanagement.application.usecase.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import com.raul.truckmanagement.application.usecase.port.GetTruckLoadsUseCasePort;
import com.raul.truckmanagement.domain.model.Load;
import com.raul.truckmanagement.domain.repository.LoadRepository;

@Service
@AllArgsConstructor
public class GetTruckLoadsUseCaseImpl implements GetTruckLoadsUseCasePort {

  private final LoadRepository loadRepository;

  @Override
  public Flux<Load> execute(UUID truckId) {
    return loadRepository.findByTruckId(truckId);
  }
}
