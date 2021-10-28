package de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent;

import de.caritas.cob.statisticsservice.api.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

  private String id;
  private UserRole userRole;

}
