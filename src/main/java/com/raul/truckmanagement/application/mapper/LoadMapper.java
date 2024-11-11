package com.raul.truckmanagement.application.mapper;

import com.raul.truckmanagement.application.dto.request.LoadRequest;
import com.raul.truckmanagement.application.dto.response.LoadResponse;
import com.raul.truckmanagement.domain.model.Load;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LoadMapper {
  LoadMapper INSTANCE = Mappers.getMapper(LoadMapper.class);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "truckId", ignore = true)
  @Mapping(target = "loadTimestamp", expression = "java(java.time.LocalDateTime.now())")
  @Mapping(target = "unloadTimestamp", ignore = true)
  Load toEntity(LoadRequest loadRequest);

  LoadResponse toResponse(Load load);
}