package de.caritas.cob.statisticsservice.config.amqpTemplate;

import java.util.HashMap;
import java.util.Map;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;

public class CustomRepublishMessageRecoverer extends RepublishMessageRecoverer {

  public CustomRepublishMessageRecoverer(AmqpTemplate errorTemplate,
      String errorExchange, String errorRoutingKey) {
    super(errorTemplate, errorExchange, errorRoutingKey);
  }

  @Override
  protected Map<? extends String, ?> additionalHeaders(Message message, Throwable cause) {
    Map<String, String> additionalHeaders = new HashMap<>();
    message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
    additionalHeaders.put("thrown exception", cause.getClass().getName());
    return additionalHeaders;
  }
}
