package technamin.services;

import com.google.gson.JsonObject;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.apache.log4j.Logger;

import java.nio.charset.StandardCharsets;

public class RabbitMQSender {
    final static Logger logger = Logger.getLogger(RabbitMQSender.class);

    public static void send(JsonObject metadata) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(Configuration.QUEUE, false, false, false, null);
            String message = metadata.toString();
            channel.basicPublish("", Configuration.QUEUE, null, message.getBytes(StandardCharsets.UTF_8));
            logger.info(" [x] Sent '" + message + "'");
        }
    }
}
