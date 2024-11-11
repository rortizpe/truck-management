package com.raul.truckmanagement.application.mapper;

import com.raul.truckmanagement.application.dto.request.TruckRequest;
import com.raul.truckmanagement.application.dto.response.TruckResponse;
import com.raul.truckmanagement.domain.model.Truck;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TruckMapper {

  TruckMapper INSTANCE = Mappers.getMapper(TruckMapper.class);

  @Mapping(target = "id", ignore = true)
  Truck toEntity(TruckRequest request);

  TruckResponse toResponse(Truck truck);
}
