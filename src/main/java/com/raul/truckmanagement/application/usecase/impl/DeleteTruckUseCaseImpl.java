package com.raul.truckmanagement.application.usecase.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;
import com.raul.truckmanagement.application.usecase.port.DeleteTruckUseCasePort;
import com.raul.truckmanagement.domain.repository.LoadRepository;
import com.raul.truckmanagement.domain.repository.TruckRepository;

@Service
@AllArgsConstructor
public class DeleteTruckUseCaseImpl implements DeleteTruckUseCasePort {

  private final TruckRepository truckRepository;
  private final LoadRepository loadRepository;

  @Override
  public Mono<Void> execute(UUID truckId) {
    return loadRepository.findByTruckId(truckId).hasElements().flatMap(hasLoads -> {
      if (Boolean.TRUE.equals(hasLoads)) {
        return Mono.error(
          new IllegalStateException("No se puede eliminar un camión con cargas activas"));
      }
      return truckRepository.deleteById(truckId);
    });
  }
}