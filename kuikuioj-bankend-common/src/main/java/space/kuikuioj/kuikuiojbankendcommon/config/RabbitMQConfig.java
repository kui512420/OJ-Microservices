package space.kuikuioj.kuikuiojbankendcommon.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ配置
 */
@Configuration
public class RabbitMQConfig {

    /**
     * 代码执行交换机
     */
    @Bean
    public DirectExchange codeExchange() {
        return new DirectExchange("code_exchange", true, false);
    }

    /**
     * 代码执行队列
     */
    @Bean
    public Queue codeQueue() {
        return new Queue("code_queue", true);
    }

    /**
     * 代码执行队列绑定
     */
    @Bean
    public Binding codeBinding() {
        return BindingBuilder.bind(codeQueue()).to(codeExchange()).with("routingkey");
    }
} 