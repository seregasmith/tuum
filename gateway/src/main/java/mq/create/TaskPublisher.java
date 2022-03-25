package mq.create;

import common.JsonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Optional;

import static common.JsonSerializer.toJson;

@Service
public class TaskPublisher {
    private static final Logger LOG = LoggerFactory.getLogger(TaskPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    @Autowired
    public TaskPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public <T, P> P publish(String queueName, T task, Class<P> payloadClass) {

        Message newMessage = MessageBuilder.withBody(toJson(task).getBytes()).build();

        LOG.info("client send：{}", newMessage);

        Message result = rabbitTemplate.sendAndReceive(queueName, newMessage);

        if (result != null) {
            String correlationId = newMessage.getMessageProperties().getCorrelationId();
            LOG.info("correlationId:{}", correlationId);

            HashMap<String, Object> headers = (HashMap<String, Object>) result.getMessageProperties().getHeaders();

            String msgId = (String) headers.get("spring_returned_message_correlation");

            if (msgId.equals(correlationId)) {
                return Optional.of(result)
                        .map(Message::getBody)
                        .map(bytes -> new String(bytes, StandardCharsets.UTF_8))
                        .map(s -> JsonSerializer.parseJson(s, payloadClass))
                        .orElse(null);

            }
        }
        return null;
    }
}
