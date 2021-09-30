package de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConsultingType {

  private int id;
  private String name;

}
