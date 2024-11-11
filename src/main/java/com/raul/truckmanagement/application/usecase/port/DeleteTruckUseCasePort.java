package com.raul.truckmanagement.application.usecase.port;

import reactor.core.publisher.Mono;
import java.util.UUID;

public interface DeleteTruckUseCasePort {
  Mono<Void> execute(UUID truckId);
}
