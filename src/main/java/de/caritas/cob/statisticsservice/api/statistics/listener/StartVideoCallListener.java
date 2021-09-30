package de.caritas.cob.statisticsservice.api.statistics.listener;

import de.caritas.cob.statisticsservice.api.model.StartVideoCallStatisticsEventMessage;
import de.caritas.cob.statisticsservice.api.service.UserStatisticsService;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.StatisticsEvent;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.StatisticsEventBuilder;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.meta.StartVideoCallMetaData;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.meta.VideoCallStatus;
import java.time.temporal.ChronoUnit;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/** AMQP Listener for start video call message statistics event. */
@Service
@RequiredArgsConstructor
public class StartVideoCallListener {

  private final @NonNull MongoTemplate mongoTemplate;
  private final @NonNull UserStatisticsService userStatisticsService;

  /**
   * Consumer for start video call statics statistics event.
   *
   * @param eventMessage the {@link StartVideoCallStatisticsEventMessage} instance
   */
  @RabbitListener(
      id = "start-video-call-event-listener",
      queues = "#{rabbitMqConfig.QUEUE_NAME_START_VIDEO_CALL}",
      containerFactory = "simpleRabbitListenerContainerFactory")
  public void receiveMessage(StartVideoCallStatisticsEventMessage eventMessage) {

    StatisticsEvent statisticsEvent =
        StatisticsEventBuilder.getInstance(
            () ->
              userStatisticsService.retrieveSessionViaSessionId(eventMessage.getSessionId()))
            .withEventType(eventMessage.getEventType())
            .withTimestamp(eventMessage.getTimestamp().truncatedTo(ChronoUnit.SECONDS).toInstant())
            .withUserId(eventMessage.getUserId())
            .withUserRole(eventMessage.getUserRole())
            .withMetaData(buildMetaData(eventMessage))
            .build();

    mongoTemplate.insert(statisticsEvent);
  }

  private StartVideoCallMetaData buildMetaData(StartVideoCallStatisticsEventMessage eventMessage) {
    return StartVideoCallMetaData.builder()
        .videoCallUuid(eventMessage.getVideoCallUuid())
        .duration(0)
        .timestampStop(null)
        .status(VideoCallStatus.ONGOING)
        .build();
  }
}
