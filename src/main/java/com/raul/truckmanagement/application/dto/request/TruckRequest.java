package com.raul.truckmanagement.application.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TruckRequest {
  private String licensePlate;
  private String model;
  private Double capacityLimit;
  private Double currentLoad;
  private String status;
}
