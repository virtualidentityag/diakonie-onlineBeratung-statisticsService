package de.caritas.cob.statisticsservice.api.statistics.listener;

import de.caritas.cob.statisticsservice.api.model.ArchiveSessionStatisticsEventMessage;
import de.caritas.cob.statisticsservice.api.service.UserStatisticsService;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.StatisticsEvent;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.StatisticsEventBuilder;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.User;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.meta.ArchiveMetaData;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/**
 * AMQP Listener for archive session statistics event.
 */
@Service
@RequiredArgsConstructor
public class AccountDeletionSessionListener {

  private final @NonNull MongoTemplate mongoTemplate;
  private final @NonNull UserStatisticsService userStatisticsService;

  /**
   * Consumer for archive session message statistics event.
   *
   * @param eventMessage the {@link ArchiveSessionStatisticsEventMessage} instance
   */
  @RabbitListener(
      id = "delete-account-event-listener",
      queues = "#{rabbitMqConfig.QUEUE_NAME_DELETE_ACCOUNT}",
      containerFactory = "simpleRabbitListenerContainerFactory")
  public void receiveMessage(ArchiveSessionStatisticsEventMessage eventMessage) {

    StatisticsEvent statisticsEvent = StatisticsEvent.builder()
        .eventType(eventMessage.getEventType())
        .timestamp(eventMessage.getTimestamp().toInstant())
        .user(User.builder().userRole(eventMessage.getUserRole()).id(eventMessage.getUserId())
            .build())
        .metaData(buildMetaData(eventMessage))
        .build();

    mongoTemplate.insert(statisticsEvent);
  }

  private ArchiveMetaData buildMetaData(ArchiveSessionStatisticsEventMessage eventMessage) {
    return ArchiveMetaData.builder()
        .endDate(eventMessage.getEndDate())
        .tenantId(eventMessage.getTenantId())
        .build();
  }
}
