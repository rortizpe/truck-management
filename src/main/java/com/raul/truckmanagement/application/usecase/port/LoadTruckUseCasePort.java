package com.raul.truckmanagement.application.usecase.port;

import reactor.core.publisher.Mono;
import com.raul.truckmanagement.application.usecase.command.LoadTruckCommand;
import com.raul.truckmanagement.domain.model.Load;

public interface LoadTruckUseCasePort {
  Mono<Load> execute(LoadTruckCommand command);
}