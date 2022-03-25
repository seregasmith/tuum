package mq.create;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitClientConfig {
    public static final String RPC_CREATE_TRANSACTION_MSG_QUEUE = "rpc_create_transaction";
    public static final String RPC_CREATE_ACCOUNT_MSG_QUEUE = "rpc_create_account";
    public static final String RPC_REPLY_MSG_QUEUE = "rpc_reply_create_transaction";
    public static final String RPC_EXCHANGE = "rpc_exchange";

    @Bean
    Queue replyQueue() {
        return new Queue(RPC_REPLY_MSG_QUEUE);
    }

    @Bean
    TopicExchange topicExchange() {
        return new TopicExchange(RPC_EXCHANGE);
    }

    @Bean
    Binding replyBinding() {
        return BindingBuilder.bind(replyQueue())
                .to(topicExchange())
                .with(RPC_REPLY_MSG_QUEUE);
    }

    @Bean
    RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setReplyAddress(RPC_REPLY_MSG_QUEUE);
        rabbitTemplate.setReplyTimeout(6000);
        return rabbitTemplate;
    }

    @Bean
    SimpleMessageListenerContainer replyContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer simpleMessageListenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        simpleMessageListenerContainer.setQueueNames(RPC_REPLY_MSG_QUEUE);
        simpleMessageListenerContainer.setMessageListener(rabbitTemplate(connectionFactory));
        return simpleMessageListenerContainer;
    }

}
