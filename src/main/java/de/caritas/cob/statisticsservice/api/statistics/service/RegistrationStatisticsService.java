package de.caritas.cob.statisticsservice.api.statistics.service;

import de.caritas.cob.statisticsservice.api.model.RegistrationStatisticsListResponseDTO;
import de.caritas.cob.statisticsservice.api.model.RegistrationStatisticsResponseDTO;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.StatisticsEvent;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.meta.RegistrationMetaData;
import de.caritas.cob.statisticsservice.api.statistics.repository.StatisticsEventRepository;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationStatisticsService {

  private final @NonNull StatisticsEventRepository statisticsEventRepository;

  private RegistrationStatisticsResponseDTO convertStatisticsEvent(
      StatisticsEvent rawEvent) {
    RegistrationMetaData metadata = (RegistrationMetaData) rawEvent.getMetaData();
    return new RegistrationStatisticsResponseDTO()
        .userId(rawEvent.getUser().getId())
        .registrationDate(metadata.getRegistrationDate())
        .age(metadata.getAge())
        .gender(metadata.getGender())
        .counsellingRelation(metadata.getCounsellingRelation())
        .mainTopicInternalAttribute(metadata.getMainTopicInternalAttribute())
        .topicsInternalAttributes(metadata.getTopicsInternalAttributes())
        .postalCode(metadata.getPostalCode());
  }

  public RegistrationStatisticsListResponseDTO fetchRegistrationStatisticsData() {
    return buildResponseDTO();
  }

  private RegistrationStatisticsListResponseDTO buildResponseDTO() {
    RegistrationStatisticsListResponseDTO registrationStatisticsList = new RegistrationStatisticsListResponseDTO();
    List<StatisticsEvent> registrationStatisticsRaw = statisticsEventRepository.getAllRegistrationStatistics();
    for (StatisticsEvent rawEvent : registrationStatisticsRaw) {
      registrationStatisticsList.addRegistrationStatisticsItem(convertStatisticsEvent(rawEvent));
    }
    return registrationStatisticsList;
  }
}