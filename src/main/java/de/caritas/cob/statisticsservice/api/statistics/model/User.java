package de.caritas.cob.statisticsservice.api.statistics.model;

import de.caritas.cob.statisticsservice.api.model.UserRole;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class User {

  private String id;
  private UserRole userRole;

}
