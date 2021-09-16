package de.caritas.cob.statisticsservice.api.statistics.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {

  private String id;
  private UserType type;

}
