package com.raul.truckmanagement.application.usecase.port;

import reactor.core.publisher.Mono;
import java.util.UUID;

public interface UnloadTruckUseCasePort {
  Mono<Void> execute(UUID truckId);
}