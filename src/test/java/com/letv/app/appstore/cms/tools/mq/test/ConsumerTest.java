package com.letv.app.appstore.cms.tools.mq.test;

import com.letv.app.appstore.cms.tools.mq.Consumer;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.junit.Test;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * Created by liuhonglin on 2017/1/10.
 */
public class ConsumerTest {

    private String brokerUrl = "tcp://192.168.17.128:61616?" +
                            "jms.optimizeAcknowledge=true" +
                            "&jms.optimizeAcknowledgeTimeOut=30000" +
                            "&jms.redeliveryPolicy.maximumRedeliveries=6";

    private String queue = "lhlTestQueue2?customer.prefetchSize=2";
    private String topic = "lhlTestTopic?customer.prefetchSize=2";

    @Test
    public void testQueue() throws JMSException {

        Consumer consumer = new Consumer(brokerUrl);
        consumer.setQueue(queue);

        ActiveMQTextMessage message = (ActiveMQTextMessage)consumer.receiveMessage();

        System.out.println(message.getText());

        consumer.close();
    }

    @Test
    public void testTopic() throws JMSException {

        Consumer consumer = new Consumer(brokerUrl);
        consumer.setTopics(topic);

        ActiveMQTextMessage message = (ActiveMQTextMessage)consumer.receiveMessage();

        System.out.println(message.getText());

        consumer.close();
    }

    @Test
    public void testListener() throws JMSException {
        Consumer consumer = new Consumer(brokerUrl);
        consumer.setQueue(queue);
        consumer.setMessageListener(new TextMessageListener());

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        consumer.close();

    }
}
