package vn.backend.ksv.common.connector.mq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import vn.backend.ksv.common.exception.CommonException;
import vn.backend.ksv.config.model.MqConfig;

import java.util.Map;

/**
 * Creator: Nguyen Quang Hung
 * Date: 8/9/23
 * Time: 2:17 PM
 */
public interface IMqAdapter {
    IMqAdapter installMQ(MqConfig config);

    void pushToMQ(String message, String queue) throws CommonException.ConnectionError;

    void pushToMQNotLog(String message, String queue) throws CommonException.ConnectionError;

    Channel getChannel();

    void pushToMQWithProperties(String message, String queue, AMQP.BasicProperties properties);

    Map<String, Object> getQueueArgs();

    void declareTemporaryExchange(String exchangeName, String workingQueue);

    void pushToExchangeWithproperties(String message, String exchange, AMQP.BasicProperties properties);

    void declareQueue(String queue);

    void declareAutoDelTemporaryExchange(String exchangeName, String workingQueue);
}
