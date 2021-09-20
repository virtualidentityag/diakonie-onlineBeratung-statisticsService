package de.caritas.cob.statisticsservice.api.statistics.events.assignsession;

import de.caritas.cob.statisticsservice.api.model.AssignSessionStatisticsEventMessage;
import de.caritas.cob.statisticsservice.api.model.UserRole;
import de.caritas.cob.statisticsservice.api.service.UserStatisticsService;
import de.caritas.cob.statisticsservice.api.statistics.model.StatisticEventBuilder;
import de.caritas.cob.statisticsservice.api.statistics.model.StatisticsEvent;
import de.caritas.cob.statisticsservice.config.RabbitMqConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/** AMQP Listener for assign session statistics event. */
@Service
@RequiredArgsConstructor
public class AssignSessionListener {

  private final @NonNull MongoTemplate mongoTemplate;
  private final @NonNull UserStatisticsService userStatisticsService;
  private @NonNull RabbitMqConfig rabbitMqConfig;

  /**
   * Consumer for assign session message statistics event.
   *
   * @param eventMessage the {@link AssignSessionStatisticsEventMessage} instance
   */
  @RabbitListener(queues = "#{rabbitMqConfig.QUEUE_NAME_ASSIGN_SESSION}")
  public void receiveMessage(AssignSessionStatisticsEventMessage eventMessage) {

    StatisticsEvent statisticsEvent =
        StatisticEventBuilder.getInstance(
            () ->
                userStatisticsService.retrieveSessionViaSessionId(eventMessage.getSessionId()))
            .withEventType(eventMessage.getEventType())
            .withTimestamp(eventMessage.getTimestamp().toInstant())
            .withUserId(eventMessage.getConsultantId())
            .withUserRole(UserRole.CONSULTANT)
            .build();

    mongoTemplate.insert(statisticsEvent);
  }
}
