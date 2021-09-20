package de.caritas.cob.statisticsservice.api.statistics.events.createmessage;

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

import de.caritas.cob.statisticsservice.api.model.CreateMessageStatisticsEventMessage;
import de.caritas.cob.statisticsservice.api.model.EventType;
import de.caritas.cob.statisticsservice.api.model.UserRole;
import de.caritas.cob.statisticsservice.api.service.UserStatisticsService;
import de.caritas.cob.statisticsservice.api.statistics.model.StatisticsEvent;
import de.caritas.cob.statisticsservice.api.statistics.model.meta.CreateMessageMetaData;
import de.caritas.cob.statisticsservice.config.RabbitMqConfig;
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
public class CreateMessageListenerTest {

  @InjectMocks
  CreateMessageListener createMessageListener;
  @Mock
  MongoTemplate mongoTemplate;
  @Mock
  UserStatisticsService userStatisticsService;
  @Mock
  RabbitMqConfig rabbitMqConfig;

  @Test
  public void receiveMessage_Should_saveEventToMongoDb() {

    SessionStatisticsResultDTO sessionStatisticsResultDTO = buildResultDto();
    when(userStatisticsService.retrieveSessionViaRcGroupId(RC_GROUP_ID))
        .thenReturn(sessionStatisticsResultDTO);

    CreateMessageStatisticsEventMessage createMessageStatisticsEventMessage = buildEventMessage();
    createMessageListener.receiveMessage(createMessageStatisticsEventMessage);
    verify(mongoTemplate, times(1)).insert(any(StatisticsEvent.class));

    ArgumentCaptor<StatisticsEvent> statisticsEventCaptor = ArgumentCaptor.forClass(StatisticsEvent.class);
    verify(mongoTemplate).insert(statisticsEventCaptor.capture());
    StatisticsEvent statisticsEvent = statisticsEventCaptor.getValue();
    assertThat(statisticsEvent.getEventType(), is(createMessageStatisticsEventMessage.getEventType()));
    assertThat(statisticsEvent.getSessionId(), is(sessionStatisticsResultDTO.getId()));
    assertThat(statisticsEvent.getConsultingType().getId(), is(sessionStatisticsResultDTO.getConsultingType()));
    assertThat(statisticsEvent.getAgency().getId(), is(sessionStatisticsResultDTO.getAgencyId()));
    assertThat(statisticsEvent.getTimestamp(), is(createMessageStatisticsEventMessage.getTimestamp().toInstant()));
    assertThat(statisticsEvent.getUser().getId(), is(createMessageStatisticsEventMessage.getConsultantId()));
    assertThat(statisticsEvent.getUser().getUserRole(), is(UserRole.CONSULTANT));
    assertThat(statisticsEvent.getMetaData(), is(buildMetaData(createMessageStatisticsEventMessage)));
  }

  private SessionStatisticsResultDTO buildResultDto() {
    return new SessionStatisticsResultDTO()
        .id(SESSION_ID)
        .isTeamSession(false)
        .agencyId(AGENCY_ID)
        .consultingType(CONSULTING_TYPE_ID)
        .rcGroupId(RC_GROUP_ID);
  }

  private CreateMessageStatisticsEventMessage buildEventMessage() {
    return new CreateMessageStatisticsEventMessage()
        .rcGroupId(RC_GROUP_ID)
        .eventType(EventType.CREATE_MESSAGE)
        .consultantId(CONSULTANT_ID)
        .timestamp(OffsetDateTime.now())
        .hasAttachment(true);
  }

  private CreateMessageMetaData buildMetaData(
      CreateMessageStatisticsEventMessage createMessageStatisticsEventMessage) {
    return CreateMessageMetaData
        .builder()
        .hasAttachment(createMessageStatisticsEventMessage.getHasAttachment())
        .build();
  }

}
