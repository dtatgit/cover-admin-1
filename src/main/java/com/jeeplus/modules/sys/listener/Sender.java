package com.jeeplus.modules.sys.listener;

import com.antu.mq.MQUtils;
import com.antu.mq.activemq.ActivemqAdapter;
import org.springframework.beans.factory.annotation.Autowired;


import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Sender {

    private static final int SEND_NUMBER = 5;

    public static void main(String[] args) {
        // ConnectionFactory ：连接工厂，JMS 用它创建连接
        ConnectionFactory connectionFactory;
        // Connection ：JMS 客户端到JMS Provider 的连接
        Connection connection = null;
        // Session： 一个发送或接收消息的线程
        Session session;
        // Destination ：消息的目的地;消息发送给谁.
        Destination destination;
        // MessageProducer：消息发送者
        MessageProducer producer;

        // 构造ConnectionFactory实例对象，此处采用ActiveMq的实现jar
        //connectionFactory = new ActiveMQConnectionFactory("test","test@ttat2020","tcp://192.168.1.32:61616");
        connectionFactory = new ActiveMQConnectionFactory("","","tcp://115.236.178.228:8132");
        try {
            // 构造从工厂得到连接对象
            connection = connectionFactory.createConnection();
            // 启动
            connection.start();
            // 获取操作连接
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
            // 获取session注意参数值xingbo.xu-queue是一个服务器的queue，须在在ActiveMq的console配置
            //destination = session.createQueue("test.dddd");//队列
            //destination = session.createTopic("test.dddd");//主题
            destination = session.createTopic("datac_user_dept_topic");//主题
            // 得到消息生成者【发送者】
            producer = session.createProducer(destination);
            // 设置不持久化，此处学习，实际根据项目决定
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
            // 构造消息，此处写死，项目就是参数，或者方法获取
            sendMessage(session,producer);
            session.commit();
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            try {
                if (null != connection)
                    connection.close();
            } catch (Throwable ignore) {
            }
        }


    }

    private static void sendMessage(Session session, MessageProducer producer) throws Exception {
        for (int i = 0; i < SEND_NUMBER; i++) {
            TextMessage message = session.createTextMessage("ActiveMQ发送的消息："+i);
            // 发送消息到目的地方
            System.out.println("发送消息,ActiveMQ发送的消息:"+i);
            producer.send(message);
        }

    }
}
