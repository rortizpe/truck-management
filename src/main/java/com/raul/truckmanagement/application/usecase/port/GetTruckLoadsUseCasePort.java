package com.raul.truckmanagement.application.usecase.port;

import com.raul.truckmanagement.domain.model.Load;
import reactor.core.publisher.Flux;
import java.util.UUID;

public interface GetTruckLoadsUseCasePort {
  Flux<Load> execute(UUID truckId);
}