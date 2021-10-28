package de.caritas.cob.statisticsservice.api.statistics.listener;

import de.caritas.cob.statisticsservice.api.model.AssignSessionStatisticsEventMessage;
import de.caritas.cob.statisticsservice.api.service.UserStatisticsService;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.StatisticsEventBuilder;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.StatisticsEvent;
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

  /**
   * Consumer for assign session message statistics event.
   *
   * @param eventMessage the {@link AssignSessionStatisticsEventMessage} instance
   */
  @RabbitListener(
      id = "assing-session-event-listener",
      queues = "#{rabbitMqConfig.QUEUE_NAME_ASSIGN_SESSION}",
      containerFactory = "simpleRabbitListenerContainerFactory")
  public void receiveMessage(AssignSessionStatisticsEventMessage eventMessage) {

    StatisticsEvent statisticsEvent =
        StatisticsEventBuilder.getInstance(
            () ->
                userStatisticsService.retrieveSessionViaSessionId(eventMessage.getSessionId()))
            .withEventType(eventMessage.getEventType())
            .withTimestamp(eventMessage.getTimestamp().toInstant())
            .withUserId(eventMessage.getUserId())
            .withUserRole(eventMessage.getUserRole())
            .build();

    mongoTemplate.insert(statisticsEvent);
  }
}
