package com.raul.truckmanagement.application.usecase.port;

import reactor.core.publisher.Flux;
import com.raul.truckmanagement.domain.model.Truck;

public interface GetAllTrucksUseCasePort {
  Flux<Truck> execute();
}