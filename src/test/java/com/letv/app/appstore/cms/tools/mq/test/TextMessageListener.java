package com.letv.app.appstore.cms.tools.mq.test;

import com.letv.app.appstore.cms.tools.mq.MyListener;
import org.apache.activemq.command.ActiveMQTextMessage;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * Created by liuhonglin on 2017/1/10.
 */
public class TextMessageListener extends MyListener {

    @Override
    public void dealMessage(Message message) {

        try {
            System.out.println(((ActiveMQTextMessage) message).getText());
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
