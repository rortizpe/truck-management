package com.raul.truckmanagement.application.usecase.port;

import com.raul.truckmanagement.domain.model.Truck;
import reactor.core.publisher.Mono;
import java.util.UUID;

public interface RegisterTruckUseCasePort {
  Mono<Truck> execute(Truck truck);
  Mono<Truck> getTruckById(UUID id);
}