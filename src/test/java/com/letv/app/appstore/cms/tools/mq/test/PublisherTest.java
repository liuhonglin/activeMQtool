package com.letv.app.appstore.cms.tools.mq.test;

import com.letv.app.appstore.cms.tools.mq.Publisher;
import org.junit.Test;

import javax.jms.JMSException;

/**
 * Created by liuhonglin on 2017/1/10.
 */
public class PublisherTest {

    @Test
    public void testQueue() throws JMSException {
        Publisher publisher = new Publisher("tcp://192.168.17.128:61616");
        publisher.setQueue("lhlTestQueue2");
        publisher.sendMessage("test8");
        publisher.close();
    }

    @Test
    public void testTopic() throws JMSException {
        Publisher publisher = new Publisher("tcp://192.168.17.128:61616");
        publisher.setTopics("lhlTestTopic");
        publisher.sendMessage("发布订阅消息3");
        publisher.close();
    }
}
