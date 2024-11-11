package com.raul.truckmanagement.domain.repository;

import com.raul.truckmanagement.domain.model.Load;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface LoadRepository extends ReactiveCrudRepository<Load, UUID> {
  Flux<Load> findByTruckId(UUID truckId);
  Mono<Load> findFirstByTruckIdOrderByLoadTimestampDesc(UUID truckId);

}
