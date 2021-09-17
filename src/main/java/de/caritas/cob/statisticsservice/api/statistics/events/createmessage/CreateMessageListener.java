package de.caritas.cob.statisticsservice.api.statistics.events.createmessage;

import de.caritas.cob.statisticsservice.api.exception.MissingConsultingTypeException;
import de.caritas.cob.statisticsservice.api.model.CreateMessageStatisticsEventMessage;
import de.caritas.cob.statisticsservice.api.model.EventType;
import de.caritas.cob.statisticsservice.api.statistics.model.EventStatus;
import de.caritas.cob.statisticsservice.api.statistics.model.StatisticsEvent;
import de.caritas.cob.statisticsservice.api.statistics.model.UserType;
import de.caritas.cob.statisticsservice.api.statistics.model.meta.CreateMessageMetaData;
import de.caritas.cob.statisticsservice.config.RabbitMqConfig;
import de.caritas.cob.statisticsservice.api.statistics.model.User;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/** AMQP Listener for create message statistics event. */
@Service
@RequiredArgsConstructor
public class CreateMessageListener {

  private @NonNull RabbitMqConfig rabbitMqConfig;
  private @NonNull MongoTemplate mongoTemplate;

  /**
   * Consumer for create message statics statistics event.
   *
   * @param eventMessage the {@link CreateMessageStatisticsEventMessage} instance
   */
  @RabbitListener(queues = "#{rabbitMqConfig.QUEUE_NAME_CREATE_MESSAGE}", containerFactory = "simpleRabbitListenerContainerFactory")
  public void receiveMessage(CreateMessageStatisticsEventMessage eventMessage)
      throws MissingConsultingTypeException {

    StatisticsEvent statisticsEvent =
        StatisticsEvent.builder()
            .eventStatus(EventStatus.VALID)
            .eventType(EventType.CREATE_MESSAGE)
            .timestamp(eventMessage.getTimestamp().toInstant())
            .user(
                User.builder().type(UserType.CONSULTANT).id(eventMessage.getConsultantId()).build())
            .metaData(
                CreateMessageMetaData.builder()
                    .hasAttachment(eventMessage.getHasAttachment())
                    .build())
            .build();

    throw new MissingConsultingTypeException("hello, world");
    //mongoTemplate.insert(statisticsEvent);
  }
}
