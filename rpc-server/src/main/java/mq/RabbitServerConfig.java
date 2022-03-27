package mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitServerConfig {
    public static final String RPC_REPLY_MSG_QUEUE = "rpc_reply";
    public static final String RPC_EXCHANGE = "rpc_exchange";
    public static final String RPC_CREATE_TRANSACTION_MSG_QUEUE = "rpc_create_transaction";
    public static final String RPC_CREATE_ACCOUNT_MSG_QUEUE = "rpc_create_account";

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

}
