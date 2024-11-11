package com.raul.truckmanagement.domain.model;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TruckEvent {
  private UUID truckId;
  private String status;
  private String description;

  @Override
  public String toString() {
    return "TruckEvent{" + "truckId=" + truckId + ", status='" + status + '\'' + ", description='"
      + description + '\'' + '}';
  }
}