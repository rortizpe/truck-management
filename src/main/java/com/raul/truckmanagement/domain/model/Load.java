package com.raul.truckmanagement.domain.model;

import java.time.LocalDateTime;
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
@Table("loads")
public class Load {

  @Id
  private UUID id;

  @Column("truck_id")
  private UUID truckId;

  private Double volume;

  private String description;

  @Column("load_timestamp")
  private LocalDateTime loadTimestamp;

  @Column("unload_timestamp")
  private LocalDateTime unloadTimestamp;

}