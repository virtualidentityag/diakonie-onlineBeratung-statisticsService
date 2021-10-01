package de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsultingType {

  private int id;
  private String name;

}
