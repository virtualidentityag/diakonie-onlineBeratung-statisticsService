package de.caritas.cob.statisticsservice.api.statistics.events.createmessage;

import de.caritas.cob.statisticsservice.api.model.CreateMessageStatisticsEventMessage;
import de.caritas.cob.statisticsservice.api.model.UserRole;
import de.caritas.cob.statisticsservice.api.service.UserStatisticsService;
import de.caritas.cob.statisticsservice.api.statistics.model.StatisticEventBuilder;
import de.caritas.cob.statisticsservice.api.statistics.model.StatisticsEvent;
import de.caritas.cob.statisticsservice.api.statistics.model.meta.CreateMessageMetaData;
import de.caritas.cob.statisticsservice.config.RabbitMqConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/** AMQP Listener for create message statistics event. */
@Service
@RequiredArgsConstructor
public class CreateMessageListener {

  private final @NonNull RabbitMqConfig rabbitMqConfig;
  private final @NonNull MongoTemplate mongoTemplate;
  private final @NonNull UserStatisticsService userStatisticsService;

  /**
   * Consumer for create message statics statistics event.
   *
   * @param eventMessage the {@link CreateMessageStatisticsEventMessage} instance
   */
  @RabbitListener(
      queues = "#{rabbitMqConfig.QUEUE_NAME_CREATE_MESSAGE}",
      containerFactory = "simpleRabbitListenerContainerFactory")
  public void receiveMessage(CreateMessageStatisticsEventMessage eventMessage) {

    StatisticsEvent statisticsEvent =
        StatisticEventBuilder.getInstance(
            () ->
                userStatisticsService.retrieveSessionViaRcGroupId(eventMessage.getRcGroupId()))
            .withEventType(eventMessage.getEventType())
            .withTimestamp(eventMessage.getTimestamp().toInstant())
            .withUserId(eventMessage.getConsultantId())
            .withUserRole(UserRole.CONSULTANT)
            .withMetaData(buildMetaData(eventMessage))
            .build();

    mongoTemplate.insert(statisticsEvent);
  }

  private CreateMessageMetaData buildMetaData(CreateMessageStatisticsEventMessage eventMessage) {
    return CreateMessageMetaData
        .builder()
        .hasAttachment(eventMessage.getHasAttachment())
        .build();
  }
}
