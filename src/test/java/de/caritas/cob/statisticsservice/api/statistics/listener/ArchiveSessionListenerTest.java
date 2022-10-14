package de.caritas.cob.statisticsservice.api.statistics.listener;

import static de.caritas.cob.statisticsservice.api.testhelper.TestConstants.AGENCY_ID;
import static de.caritas.cob.statisticsservice.api.testhelper.TestConstants.CONSULTANT_ID;
import static de.caritas.cob.statisticsservice.api.testhelper.TestConstants.CONSULTING_TYPE_ID;
import static de.caritas.cob.statisticsservice.api.testhelper.TestConstants.RC_GROUP_ID;
import static de.caritas.cob.statisticsservice.api.testhelper.TestConstants.SESSION_ID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.caritas.cob.statisticsservice.api.model.ArchiveSessionStatisticsEventMessage;
import de.caritas.cob.statisticsservice.api.model.EventType;
import de.caritas.cob.statisticsservice.api.model.UserRole;
import de.caritas.cob.statisticsservice.api.service.UserStatisticsService;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.StatisticsEvent;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.meta.ArchiveMetaData;
import de.caritas.cob.statisticsservice.userstatisticsservice.generated.web.model.SessionStatisticsResultDTO;
import java.time.OffsetDateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class ArchiveSessionListenerTest {

  @InjectMocks
  ArchiveSessionListener archiveSessionListener;
  @Mock
  MongoTemplate mongoTemplate;
  @Mock
  UserStatisticsService userStatisticsService;

  @Test
  public void receiveMessage_Should_saveEventToMongoDb() {

    SessionStatisticsResultDTO sessionStatisticsResultDTO = buildResultDto();
    when(userStatisticsService.retrieveSessionViaSessionId(SESSION_ID))
        .thenReturn(sessionStatisticsResultDTO);

    ArchiveSessionStatisticsEventMessage archiveSessionStatisticsEventMessage = buildEventMessage();
    archiveSessionListener.receiveMessage(archiveSessionStatisticsEventMessage);
    verify(mongoTemplate, times(1)).insert(any(StatisticsEvent.class));

    ArgumentCaptor<StatisticsEvent> statisticsEventCaptor = ArgumentCaptor.forClass(StatisticsEvent.class);
    verify(mongoTemplate).insert(statisticsEventCaptor.capture());
    StatisticsEvent statisticsEvent = statisticsEventCaptor.getValue();
    assertThat(statisticsEvent.getEventType(), is(archiveSessionStatisticsEventMessage.getEventType()));
    assertThat(statisticsEvent.getSessionId(), is(sessionStatisticsResultDTO.getId()));
    assertThat(statisticsEvent.getConsultingType().getId(), is(sessionStatisticsResultDTO.getConsultingType()));
    assertThat(statisticsEvent.getAgency().getId(), is(sessionStatisticsResultDTO.getAgencyId()));
    assertThat(statisticsEvent.getTimestamp(), is(archiveSessionStatisticsEventMessage.getTimestamp().toInstant()));
    assertThat(statisticsEvent.getUser().getId(), is(archiveSessionStatisticsEventMessage.getUserId()));
    assertThat(statisticsEvent.getUser().getUserRole(), is(UserRole.CONSULTANT));
    assertThat(statisticsEvent.getMetaData(), is(buildMetaData()));
  }

  private SessionStatisticsResultDTO buildResultDto() {
    return new SessionStatisticsResultDTO()
        .id(SESSION_ID)
        .isTeamSession(false)
        .agencyId(AGENCY_ID)
        .consultingType(CONSULTING_TYPE_ID)
        .rcGroupId(RC_GROUP_ID);
  }

  private ArchiveSessionStatisticsEventMessage buildEventMessage() {
    return new ArchiveSessionStatisticsEventMessage()
        .sessionId(SESSION_ID)
        .eventType(EventType.ASSIGN_SESSION)
        .userId(CONSULTANT_ID)
        .userRole(UserRole.CONSULTANT)
        .timestamp(OffsetDateTime.now())
        .endDate("2022-10-14T10:43:29");
  }

  private ArchiveMetaData buildMetaData() {
    return ArchiveMetaData.builder()
        .endDate("2022-10-14T10:43:29")
        .build();
  }
}
