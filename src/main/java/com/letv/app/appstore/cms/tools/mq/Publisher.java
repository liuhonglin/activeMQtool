package com.letv.app.appstore.cms.tools.mq;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.*;

/**
 * Created by liuhonglin on 2017/1/10.
 */
public class Publisher {

    private String brokerURL;
    private ConnectionFactory factory;
    private Connection connection;
    private Session session;
    private MessageProducer producer;
    private Destination destination;

    public Publisher(String brokerURL) throws JMSException {
        this.brokerURL = brokerURL;

        factory = new ActiveMQConnectionFactory(brokerURL);
        connection = factory.createConnection();
        try {
            connection.start();
        } catch (JMSException jmse) {
            connection.close();
            throw jmse;
        }
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        producer = session.createProducer(null);
    }

    /**
     * 发布 - 订阅
     * @param topicUri
     * @throws JMSException
     */
    public void setTopics(String topicUri) throws JMSException {
        destination = session.createTopic(topicUri);
    }

    /**
     * P2P
     * @param queueUri
     * @throws JMSException
     */
    public void setQueue(String queueUri) throws JMSException {
        destination = session.createQueue(queueUri);
    }

    public void sendMessage(String textMessage) throws JMSException {
        Message message = session.createTextMessage(textMessage);
        System.out.println("Sending: " + ((ActiveMQTextMessage)message).getText() + " on destination: " + destination);
        producer.send(destination, message);
    }

    public void sendMessage(String[] stocks) throws JMSException {
        for(int i = 0; i < stocks.length; i++) {
            Message message = createStockMessage(stocks[i], session);
            System.out.println("Sending: " + ((ActiveMQMapMessage)message).getContentMap() + " on destination: " + destination);
            producer.send(destination, message);
        }
    }

    protected Message createStockMessage(String stock, Session session) throws JMSException {
        MapMessage message = session.createMapMessage();
        message.setString("stock", stock);
        message.setDouble("price", 1.00);
        message.setDouble("offer", 0.01);
        message.setBoolean("up", true);

        return message;
    }

    public void close() throws JMSException {
        if (producer != null) {
            producer.close();
        }
        if (session != null) {
            session.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

}
