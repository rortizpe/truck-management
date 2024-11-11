package com.raul.truckmanagement.application.dto.response;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import com.raul.truckmanagement.domain.model.TruckStatus;

@Getter
@Builder
public class TruckResponse {
  private UUID id;
  private String licensePlate;
  private String model;
  private Double capacityLimit;
  private Double currentLoad;
  private TruckStatus status;
}
