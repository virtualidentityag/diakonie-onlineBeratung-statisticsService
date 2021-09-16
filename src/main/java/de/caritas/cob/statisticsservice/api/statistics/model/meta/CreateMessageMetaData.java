package de.caritas.cob.statisticsservice.api.statistics.model.meta;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateMessageMetaData {

  private boolean hasAttachment;

}
