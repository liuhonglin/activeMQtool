package com.letv.app.appstore.cms.tools.mq;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Created by liuhonglin on 2017/1/10.
 */
public abstract class MyListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        dealMessage(message);
    }

    public abstract void dealMessage(Message message);

}
