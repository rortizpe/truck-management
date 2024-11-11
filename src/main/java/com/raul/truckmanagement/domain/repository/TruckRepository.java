package com.raul.truckmanagement.domain.repository;

import com.raul.truckmanagement.domain.model.Truck;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface TruckRepository extends ReactiveCrudRepository<Truck, UUID> {
  Flux<Truck> findByStatus(String status);
}
