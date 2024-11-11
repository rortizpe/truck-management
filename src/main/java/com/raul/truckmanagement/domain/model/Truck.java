package com.raul.truckmanagement.domain.model;

import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Table("trucks")
public class Truck {

  @Id
  private UUID id;

  @Column("license_plate")
  private String licensePlate;

  private String model;

  @Column("capacity_limit")
  private Double capacityLimit;

  @Column("current_load")
  private Double currentLoad;

  private TruckStatus status;

}