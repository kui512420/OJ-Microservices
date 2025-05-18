package space.kuikuioj.kuikuiojbankendquestionsubmit.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author kuikui
 * @date 2025/4/17 14:17
 */
public class RabbitMqConfig {
    private static final String EXCHANGE_NAME = "code_exchange";
    private static final String QUEUE_NAME = "code_queue";
    private static final String ROUTINGKEY = "routingkey";
    public static void initRabbitMqConfig() {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            // 创建一个队列
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTINGKEY);
        }catch (Exception e) {
        }
    }
}
