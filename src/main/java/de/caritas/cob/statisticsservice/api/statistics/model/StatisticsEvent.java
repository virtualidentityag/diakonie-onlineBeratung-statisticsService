package de.caritas.cob.statisticsservice.api.statistics.model;

import de.caritas.cob.statisticsservice.api.model.EventType;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "statistics_event")
@Builder
@Data
public class StatisticsEvent {

  @Id
  private String id;
  private EventType eventType;
  private long sessionId;
  private Instant timestamp;
  private ConsultingType consultingType;
  private User user;
  private Agency agency;
  private Diocese diocese;
  private Object metaData;
  private EventStatus eventStatus;


}
