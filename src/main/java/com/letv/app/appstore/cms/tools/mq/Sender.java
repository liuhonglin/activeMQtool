package com.letv.app.appstore.cms.tools.mq;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * Created by liuhonglin on 2017/1/5.
 * @See http://activemq.apache.org/hello-world.html
 * @See http://shift-alt-ctrl.iteye.com/blog/2020182
 * @See http://shmilyaw-hotmail-com.iteye.com/blog/1897635
 */
@Deprecated
public class Sender {

    public static void send(String brokerUrl, String queue, String message) {

        // ConnectionFactory ：连接工厂，JMS 用它创建连接
        ConnectionFactory connectionFactory;
        // Provider 的连接
        Connection connection = null;
        // Session： 一个发送或接收消息的线程
        Session session = null;
        // Destination ：消息的目的地;消息发送给谁.
        Destination destination = null;
        // MessageProducer：消息发送者
        MessageProducer producer;
        // TextMessage message;

        connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD, brokerUrl);

        try {
            // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();


            // 获取操作连接，第一个参数是是否使用事务，第二个参数是消费者向发送者确认消息已经接收的方式. transacted：交易，办理；acknowledge:确认。
            //第一个参数:是否支持事务，如果为true，则会忽略第二个参数，被jms服务器设置为SESSION_TRANSACTED
            //第一个参数为false时，paramB的值可为Session.AUTO_ACKNOWLEDGE，Session.CLIENT_ACKNOWLEDGE，DUPS_OK_ACKNOWLEDGE其中一个。
            // 确认消息的方式有如下三种：
            // AUTO_ACKNOWLEDGE=1(自动通知) 客户端发送和接收消息不需要做额外的工作。哪怕是接收端发生异常，也会被当作正常发送成功。
            // CLIENT_ACKNOWLEDGE=2(客户端自行决定通知时机) 此方式针对消息消费者
            // DUPS_OK_ACKNOWLEDGE=3(延时//批量通知)
            // SESSION_TRANSACTED=0(事务提交并确认)
            // INDIVIDUAL_ACKNOWLEDGE = 4(单条消息确认) - AcitveMQ补充了一个自定义的ACK模式。
            // 如果使用的是 客户端自行决定通知时机 方式，那么需要在MessageListener里显式调用message.acknowledge()来通知服务器。服务器接收到通知后采取相应的操作。
            // 事务的支持,也被控制在session级别,在事务类型的session中,消息的确认时机将和事务提交的时机保持一致.session并没有start()方法,默认每次commit之后就会开启一个新的事务.
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);

            // 创建一个名称为myTest的Queues，接收信息的时候需要与这个Queues名字一致
            destination = session.createQueue(queue);

            // 得到消息生成者【发送者】
            producer = session.createProducer(destination);
            //设置生产者的模式，有两种可选
            //DeliveryMode.PERSISTENT 当activemq关闭的时候，队列数据将会被保存
            //DeliveryMode.NON_PERSISTENT 当activemq关闭的时候，队列里面的数据将会被清空
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            // 启动
            connection.start();

            sendMessage(session, producer, message);

            session.commit();
            // Clean up
            //session.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void sendMessage(Session session, MessageProducer producer, String content) throws Exception {
        TextMessage message = session.createTextMessage(content);
        // 发送消息到目的地方
        System.out.println("发送消息：" + content);
        producer.send(message);
    }

    public static void main(String[] args) {
        send("tcp://192.168.17.128:61616", "lhlTestQueue", "测试消息");
    }

}
