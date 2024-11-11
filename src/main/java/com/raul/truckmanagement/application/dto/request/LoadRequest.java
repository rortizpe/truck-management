package com.raul.truckmanagement.application.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoadRequest {
  private Double volume;
  private String description;
}
