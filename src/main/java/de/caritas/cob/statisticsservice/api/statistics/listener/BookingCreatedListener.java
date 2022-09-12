package de.caritas.cob.statisticsservice.api.statistics.listener;

import de.caritas.cob.statisticsservice.api.model.BookingCreatedStatisticsEventMessage;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.StatisticsEvent;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.StatisticsEventBuilder;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.meta.BookingCreatedMetaData;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.meta.CreateMessageMetaData;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

/** AMQP Listener for create message statistics event. */
@Service
@RequiredArgsConstructor
public class BookingCreatedListener {

  private final @NonNull MongoTemplate mongoTemplate;

  /**
   * Consumer for create message statics statistics event.
   *
   * @param eventMessage the {@link BookingCreatedStatisticsEventMessage} instance
   */
  @RabbitListener(
      id = "booking-created-event-listener",
      queues = "#{rabbitMqConfig.QUEUE_NAME_BOOKING_CREATED}",
      containerFactory = "simpleRabbitListenerContainerFactory")
  public void receiveMessage(BookingCreatedStatisticsEventMessage eventMessage) {

    StatisticsEvent statisticsEvent = new StatisticsEvent();

    mongoTemplate.insert(statisticsEvent);
  }

  private BookingCreatedMetaData buildMetaData(BookingCreatedStatisticsEventMessage eventMessage) {
    return BookingCreatedMetaData.builder().build();
  }
}
