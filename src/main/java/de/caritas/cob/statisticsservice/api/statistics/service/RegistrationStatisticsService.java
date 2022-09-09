package de.caritas.cob.statisticsservice.api.statistics.service;

import de.caritas.cob.statisticsservice.api.model.RegistrationStatisticsListResponseDTO;
import de.caritas.cob.statisticsservice.api.model.RegistrationStatisticsResponseDTO;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.StatisticsEvent;
import de.caritas.cob.statisticsservice.api.statistics.repository.StatisticsEventRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

/**
 * Service for statistical requests.
 */
@Service
@RequiredArgsConstructor
public class RegistrationStatisticsService {

  private final @NonNull StatisticsEventRepository statisticsEventRepository;

  public RegistrationStatisticsListResponseDTO fetchRegistrationStatisticsData() {
    return buildResponseDTO();
  }

  private RegistrationStatisticsListResponseDTO buildResponseDTO() {
    RegistrationStatisticsListResponseDTO registrationStatisticsList = new RegistrationStatisticsListResponseDTO();
    List<StatisticsEvent> registrationStatisticsRaw = statisticsEventRepository.getAllRegistrationStatistics();
    for (StatisticsEvent rawEvent : registrationStatisticsRaw) {
      JSONObject metadata = new JSONObject(rawEvent.getMetaData());
      JSONArray topicsInternalAttributesRaw = metadata.getJSONArray("topicsInternalAttributes");
      List<String> topicsInternalAttributes = new ArrayList<>();
      for (int i = 0; i < topicsInternalAttributesRaw.length(); i++) {
        topicsInternalAttributes.add(topicsInternalAttributesRaw.getString(i));
      }
      registrationStatisticsList.addRegistrationStatisticsItem(
          new RegistrationStatisticsResponseDTO()
              .userId(rawEvent.getUser().getId())
              .registrationDate(metadata.getString("registrationDate"))
              .age(metadata.getInt("age"))
              .gender(metadata.getString("gender"))
              .counsellingRelation(metadata.getString("counsellingRelation"))
              .mainTopicInternalAttribute(metadata.getString("mainTopicInternalAttribute"))
              .topicsInternalAttributes(topicsInternalAttributes)
              .postalCode(metadata.getString("postalCode"))
      );
    }
    return registrationStatisticsList;
  }
}