package de.caritas.cob.statisticsservice.api.statistics.listener;

import de.caritas.cob.statisticsservice.api.model.BookingRescheduledStatisticsEventMessage;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.StatisticsEvent;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.User;
import de.caritas.cob.statisticsservice.api.statistics.model.statisticsevent.meta.BookingRescheduledMetaData;
import java.time.Instant;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/**
 * AMQP Listener for create message statistics event.
 */
@Service
@RequiredArgsConstructor
public class BookingRescheduledListener {

  private final @NonNull MongoTemplate mongoTemplate;

  /**
   * Consumer for create message statics statistics event.
   *
   * @param eventMessage the {@link BookingRescheduledStatisticsEventMessage} instance
   */
  @RabbitListener(
      id = "booking-rescheduled-event-listener",
      queues = "#{rabbitMqConfig.QUEUE_NAME_BOOKING_RESCHEDULED}",
      containerFactory = "simpleRabbitListenerContainerFactory")
  public void receiveMessage(BookingRescheduledStatisticsEventMessage eventMessage) {

    StatisticsEvent statisticsEvent = StatisticsEvent.builder()
        .eventType(eventMessage.getEventType())
        .timestamp(eventMessage.getTimestamp().toInstant())
        .user(User.builder().userRole(eventMessage.getUserRole()).id(eventMessage.getUserId())
            .build())
        .metaData(buildMetaData(eventMessage))
        .build();

    mongoTemplate.insert(statisticsEvent);
    mongoTemplate.updateMulti(
        new Query(new Criteria().orOperator(Criteria.where("metaData.bookingId").is(eventMessage.getPrevBookingId()),
            Criteria.where("metaData.currentBookingId").is(eventMessage.getPrevBookingId()))),
        new Update().set("metaData.currentBookingId", eventMessage.getBookingId()),
        StatisticsEvent.class
    );
  }

  private BookingRescheduledMetaData buildMetaData(
      BookingRescheduledStatisticsEventMessage eventMessage) {
    return BookingRescheduledMetaData.builder()
        .startTime(Instant.parse(eventMessage.getStartTime()))
        .endTime(Instant.parse(eventMessage.getEndTime()))
        .uid(eventMessage.getUid())
        .bookingId(eventMessage.getBookingId())
        .currentBookingId(eventMessage.getBookingId())
        .build();
  }
}
