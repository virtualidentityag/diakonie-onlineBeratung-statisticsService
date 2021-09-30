package de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.meta;

import java.time.Instant;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StartVideoCallMetaData {

  private String videoCallUuid;
  private long duration;
  private Instant timestampStop;
  private VideoCallStatus status;

}
