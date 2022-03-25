package mq;

import common.JsonSerializer;
import model.service.AccountDbService;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.dto.CreateTransactionPayload;
import service.dto.CreateTransactionServiceRequest;

import java.nio.charset.StandardCharsets;

import static common.JsonSerializer.toJson;

@Service
public class SaveTransactionTaskExecutor {
    private final RabbitTemplate rabbitTemplate;
    private final AccountDbService accountDbService;

    @Autowired
    public SaveTransactionTaskExecutor(RabbitTemplate rabbitTemplate,
                                       AccountDbService accountDbService) {
        this.rabbitTemplate = rabbitTemplate;
        this.accountDbService = accountDbService;
    }

    @RabbitListener(queues = RabbitServerConfig.RPC_CREATE_TRANSACTION_MSG_QUEUE)
    public void process(Message msg) {
        String json = new String(msg.getBody(), StandardCharsets.UTF_8);
        CreateTransactionServiceRequest serviceRequest = JsonSerializer.parseJson(json, CreateTransactionServiceRequest.class);

        CreateTransactionPayload payload = accountDbService.processTransaction(serviceRequest);
        Message response = MessageBuilder.withBody(toJson(payload).getBytes()).build();
        CorrelationData correlationData = new CorrelationData(msg.getMessageProperties().getCorrelationId());
        rabbitTemplate.sendAndReceive(RabbitServerConfig.RPC_REPLY_MSG_QUEUE, response, correlationData);
    }
}
