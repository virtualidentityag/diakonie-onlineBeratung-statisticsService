package de.caritas.cob.statisticsservice.api.statistics.service;

import de.caritas.cob.statisticsservice.api.helper.RegistrationStatisticsDTOConverter;
import de.caritas.cob.statisticsservice.api.model.RegistrationStatisticsListResponseDTO;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.StatisticsEvent;
import de.caritas.cob.statisticsservice.api.statistics.repository.StatisticsEventRepository;
import de.caritas.cob.statisticsservice.api.statistics.repository.StatisticsEventTenantAwareRepository;
import de.caritas.cob.statisticsservice.api.tenant.TenantContext;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationStatisticsService {


  @Value("${multitenancy.enabled}")
  private Boolean multitenancyEnabled;

  private static final Long TECHNICAL_TENANT_ID = 0L;

  private final @NonNull StatisticsEventRepository statisticsEventRepository;

  private final @NonNull StatisticsEventTenantAwareRepository statisticsEventTenantAwareRepository;

  private final @NonNull RegistrationStatisticsDTOConverter registrationStatisticsDTOConverter;

  public RegistrationStatisticsListResponseDTO fetchRegistrationStatisticsData() {
    return buildResponseDTO();
  }

  private RegistrationStatisticsListResponseDTO buildResponseDTO() {
    Map<Long, StatisticsEvent> archiveSessionLookup = getArchiveSessionLookup();

    RegistrationStatisticsListResponseDTO registrationStatisticsList = new RegistrationStatisticsListResponseDTO();
    getAllRegistrationStatistics()
        .stream()
        .map(rawEvent -> registrationStatisticsDTOConverter.convertStatisticsEvent(rawEvent, archiveSessionLookup))
        .forEach(registrationStatisticsList::addRegistrationStatisticsItem);

    return registrationStatisticsList;
  }

  private List<StatisticsEvent> getAllRegistrationStatistics() {
    if (isAllTenantAccessContext()) {
      return getAllRegistrationStatisticsForAllTenants();
    } else {
      return getRegistrationStatisticsForCurrentTenant();
    }
  }

  private List<StatisticsEvent> getAllArchiveSessionEvents() {
    if (isAllTenantAccessContext()) {
      return getAllArchiveSessionEventsForAllTenants();
    } else {
      return getArchiveSessionEventsForCurrentTenant();
    }
  }

  private List<StatisticsEvent> getRegistrationStatisticsForCurrentTenant() {
    log.info("Gathering registration statistics for tenant with id {}", TenantContext.getCurrentTenant());
    return statisticsEventTenantAwareRepository.getAllRegistrationStatistics(
        TenantContext.getCurrentTenant());
  }

  private List<StatisticsEvent> getAllRegistrationStatisticsForAllTenants() {
    log.info("Gathering registration statistics for all tenants");
    return statisticsEventRepository.getAllRegistrationStatistics();
  }

  private List<StatisticsEvent> getAllArchiveSessionEventsForAllTenants() {
    log.info("Gathering archive sessions events for all tenants");
    return statisticsEventRepository.getAllArchiveSessionEvents();
  }

  private List<StatisticsEvent> getArchiveSessionEventsForCurrentTenant() {
    log.info("Gathering archive session events for tenant with id {}", TenantContext.getCurrentTenant());
    return statisticsEventTenantAwareRepository.getAllArchiveSessionEvents(TenantContext.getCurrentTenant());
  }

  private Map<Long, StatisticsEvent> getArchiveSessionLookup() {
    return getAllArchiveSessionEvents().stream()
        .collect(Collectors.toMap(StatisticsEvent::getSessionId, Function.identity()));
  }


  private boolean isAllTenantAccessContext() {
    return multitenancyIsDisabled() || TECHNICAL_TENANT_ID.equals(TenantContext.getCurrentTenant());
  }

  private boolean multitenancyIsDisabled() {
    return !multitenancyEnabled;
  }
}
