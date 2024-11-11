package com.raul.truckmanagement.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class LoadResponse {
  private UUID id;
  private UUID truckId;
  private Double volume;
  private String description;
  private LocalDateTime loadTimestamp;
  private LocalDateTime unloadTimestamp;
}
