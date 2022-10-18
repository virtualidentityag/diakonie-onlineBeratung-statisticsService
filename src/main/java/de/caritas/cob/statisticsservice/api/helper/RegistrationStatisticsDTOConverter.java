package de.caritas.cob.statisticsservice.api.helper;

import static java.util.Objects.nonNull;

import de.caritas.cob.statisticsservice.api.model.RegistrationStatisticsResponseDTO;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.StatisticsEvent;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.meta.ArchiveMetaData;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.meta.RegistrationMetaData;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class RegistrationStatisticsDTOConverter {

  public RegistrationStatisticsResponseDTO convertStatisticsEvent(
      StatisticsEvent rawEvent, Map<Long, StatisticsEvent> archiveSessionLookup) {
    RegistrationMetaData metadata = (RegistrationMetaData) rawEvent.getMetaData();
    return new RegistrationStatisticsResponseDTO()
        .userId(rawEvent.getUser().getId())
        .registrationDate(metadata.getRegistrationDate())
        .age(metadata.getAge())
        .gender(metadata.getGender())
        .counsellingRelation(metadata.getCounsellingRelation())
        .mainTopicInternalAttribute(metadata.getMainTopicInternalAttribute())
        .topicsInternalAttributes(metadata.getTopicsInternalAttributes())
        .endDate(findEndDate(rawEvent.getSessionId(), archiveSessionLookup))
        .postalCode(metadata.getPostalCode());
  }

  private String findEndDate(Long sessionId, Map<Long, StatisticsEvent> archiveSessionLookup) {
    if (nonNull(archiveSessionLookup) && archiveSessionLookup.containsKey(sessionId)) {
      ArchiveMetaData metaData = (ArchiveMetaData) archiveSessionLookup.get(sessionId).getMetaData();
      return metaData.getEndDate();
    }
    return null;
  }

}
