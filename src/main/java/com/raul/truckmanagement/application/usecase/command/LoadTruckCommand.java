package com.raul.truckmanagement.application.usecase.command;

import com.raul.truckmanagement.domain.model.Load;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoadTruckCommand {
  private final UUID truckId;
  private final Load load;

}
