package com.letv.app.appstore.cms.tools.mq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by liuhonglin on 2017/1/10.
 */
public class Consumer {

    private String brokerURL;
    private ConnectionFactory factory;
    private Connection connection;
    private Session session;
    private Destination destination;

    public Consumer(String brokerURL) throws JMSException {
        factory = new ActiveMQConnectionFactory(brokerURL);
        connection = factory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
    }

    public void setTopics(String topicUri) throws JMSException {
        destination = session.createTopic(topicUri);
    }

    public void setQueue(String queueUri) throws JMSException {
        destination = session.createQueue(queueUri);
    }

    public void setMessageListener(MessageListener listener) throws JMSException {
        MessageConsumer messageConsumer = session.createConsumer(destination);
        messageConsumer.setMessageListener(listener);
    }

    public Message receiveMessage() throws JMSException {
        MessageConsumer messageConsumer = session.createConsumer(destination);
        return messageConsumer.receive();
    }

    public void close() throws JMSException {
        if (session != null) {
            session.close();
        }
        if (connection != null) {
            connection.close();
        }
    }
}
