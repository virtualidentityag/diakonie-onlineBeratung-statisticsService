package de.caritas.cob.statisticsservice.api.statistics.events.assignsession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.caritas.cob.statisticsservice.api.model.AssignSessionStatisticsEventMessage;
import de.caritas.cob.statisticsservice.config.RabbitMqConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * AMQP Listener for assign session statistics event.
*/
@Service
@RequiredArgsConstructor
public class AssignSessionListener {

  private @NonNull RabbitMqConfig rabbitMqConfig;

  /**
   * Consumer for assign session message statistics event.
   * @param message the message
   */
  @RabbitListener(queues = "#{rabbitMqConfig.QUEUE_NAME_ASSIGN_SESSION}")
  public void receiveMessage(String message) {

    try {
      AssignSessionStatisticsEventMessage assignSessionStatisticsEventMessage =
          new ObjectMapper()
              .registerModule(new JavaTimeModule())
              .readValue(message, AssignSessionStatisticsEventMessage.class);
      System.out.println(assignSessionStatisticsEventMessage);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

}
