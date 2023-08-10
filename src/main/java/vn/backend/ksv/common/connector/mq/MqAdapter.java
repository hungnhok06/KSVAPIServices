package vn.backend.ksv.common.connector.mq;

import com.rabbitmq.client.*;
import vn.backend.ksv.common.LogAdapter;
import vn.backend.ksv.common.exception.CommonException;
import vn.backend.ksv.config.model.MqConfig;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 2:20 PM
 */
public class MqAdapter implements IMqAdapter{

    private Channel channel;
    private Map<String, Object> queueArgs;
    public final LogAdapter LOGGER = LogAdapter.newInstance(this.getClass());

    @Override
    public IMqAdapter installMQ(MqConfig config) throws CommonException.ConnectionError {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(config.getHost());
            factory.setPort(config.getPort());
            factory.setUsername(config.getUser());
            factory.setPassword(config.getPassword());
            factory.setVirtualHost(config.getVirtualHost());
            factory.setConnectionTimeout(config.getConnectionTimeout());
            factory.setAutomaticRecoveryEnabled(config.getAutomaticRecoveryEnabled());
            Connection connection = factory.newConnection();
            channel = connection.createChannel();
            Map<String, Object> queueArgs = new HashMap<String, Object>();
            queueArgs.put("x-message-ttl", config.getTimeToLive());
            queueArgs.put("x-delayed-type", "direct");
            this.queueArgs = queueArgs;
        } catch (CommonException.ValidationError e) {
            LOGGER.error("MQ Config error: {}", e.getMessage());
            throw new CommonException.ConnectionError("Validation error cause by " + e.getCause());
        } catch (IOException e) {
            LOGGER.error("IO exception with message {} ", e.getMessage());
            e.printStackTrace();
            throw new CommonException.ConnectionError("IO exception " + e.getMessage() + " cause by " + e.getCause());
        } catch (TimeoutException e) {
            LOGGER.error("Time out exception with message {} ", e.getMessage());
            throw new CommonException.ConnectionError("Time out exception " + e.getMessage() + " cause by " + e.getCause());
        } catch (Exception e) {
            LOGGER.error("Unknown exception with message {} ", e.getMessage());
            throw new CommonException.ConnectionError("Unknown error " + e.getMessage() + " cause by " + e.getCause());
        }
        return this;
    }

    @Override
    public void pushToMQ(String message, String queue) throws CommonException.ConnectionError {
        try {
            channel.basicPublish("", queue, null, message.getBytes());
            LOGGER.debug("Send message {} to queue {}", message, queue);
        } catch (IOException e) {
            LOGGER.error("Error with queue {} cause by {}", queue, e.getMessage());
        }
    }

    @Override
    public void pushToMQNotLog(String message, String queue) throws CommonException.ConnectionError {
        try {
            channel.basicPublish("", queue, null, message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    public Map<String, Object> getQueueArgs() {
        return queueArgs;
    }

    @Override
    public void pushToMQWithProperties(String message, String queue, AMQP.BasicProperties properties) {
        try {
            channel.basicPublish("", queue, properties, message.getBytes());
            LOGGER.debug("Send message {} to queue {}", message, queue);
        } catch (Exception e) {
            LOGGER.error("Error on push to mq with properties cause by {}", e.getMessage());
        }

    }

    public void pushRPC(String message, String queue, String corrId, IResultHandler result) {
        try {
            LOGGER.debug("Send message {}", message);
            LOGGER.debug("Send corrID {}", corrId);
            String replyQueueName = queue + "_" + corrId + "_rpc";
            AMQP.BasicProperties props = new AMQP.BasicProperties
                    .Builder()
                    .correlationId(corrId)
                    .replyTo(replyQueueName)
                    .build();
            channel.queueDeclare(replyQueueName, false, true, true, null);
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    try {
                        LOGGER.debug("Corr accept: {}, Corr receive {}", corrId, properties.getCorrelationId());
                        if (corrId.equals(properties.getCorrelationId())) {
                            String messRes = new String(body, "UTF-8");
                            LOGGER.debug("Response message {}", messRes);
                            result.handle(messRes);
                            channel.basicCancel(consumerTag);
                        }
                    } catch (Exception e) {
                        LOGGER.error("Error on handle rpc queue");
                    }
                }
            };
            channel.basicConsume(replyQueueName, true, consumer);
            channel.basicPublish("", queue, props, message.getBytes("UTF-8"));
        } catch (Exception e) {
            LOGGER.error("Error on send message with rpc queue cause by {}", e.getMessage());
        }
    }

    @Override
    public void declareTemporaryExchange(String exchangeName, String workingQueue) {
        /**
         * This function declare temporary exchange and binding it to workingQueue
         * Use this function when you want message retry handling
         * */
        try {
            channel.exchangeDeclare(exchangeName, "x-delayed-message", true, false, queueArgs);
            channel.queueDeclare(workingQueue, false, false, false, null);
            channel.queueBind(workingQueue, exchangeName, "");
        } catch (Exception e) {
            LOGGER.error("can not declare temporaryExchange and working queue");
        }
    }

    @Override
    public void pushToExchangeWithproperties(String message, String exchange, AMQP.BasicProperties properties) {
        try {
            channel.basicPublish(exchange, "", properties, message.getBytes("UTF-8"));
            LOGGER.debug("push to Exchange: {} success with data: {}", exchange, message);
        } catch (Exception e) {
            LOGGER.error("unknown error {}", e.getMessage());
        }
    }

    @Override
    public void declareQueue(String queue) {
        try {
            channel.queueDeclare(queue, false, false, false, null);
        } catch (IOException e) {
            LOGGER.error("Error with declare queue cause by {}", e.getMessage());
        }
    }

    @Override
    public void declareAutoDelTemporaryExchange(String exchangeName, String workingQueue) {
        try {
            channel.exchangeDeclare(exchangeName, "x-delayed-message", true, true, queueArgs);
            channel.queueDeclare(workingQueue, false, false, true, null);
            channel.queueBind(workingQueue, exchangeName, "");
        } catch (Exception e) {
            LOGGER.error("can not declare temporaryExchange and working queue: {}", e.getMessage());
        }
    }

    @FunctionalInterface
    public interface IResultHandler {
        void handle(Object result);
    }
}
