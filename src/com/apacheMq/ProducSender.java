package com.apacheMq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 消息的生产者（用于向Active MQ 发送消息）
 * 
 * @author zhuss
 * 
 */
public class ProducSender {

	public static void main(String[] args) throws JMSException {
		/**
		 * ActiveMQConnectionFactory:ActiveMQ的连接的工厂类
		 * 
		 * ActiveMQConnection.DEFAULT_USER:ActivieMQ默认的用户名
		 * ActiveMQConnection.DEFAULT_PASSWORD:ActiveMQ默认的密码
		 */
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, "tcp://127.0.0.1:61616");
		/**
		 * 从链接工厂中获得链接（Connection）
		 */
		Connection connection = connectionFactory.createConnection();
		connection.start();

		/**
		 * 从连接中获得Session
		 */
		Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);

		//  Destination ：消息的目的地;消息发送给谁.  
		//  获取session注意参数值my-queue是Query的名字
		Destination destination = session.createQueue("golden001");
		/**
		 * 创建消息生产者用于发送消息
		 */
		MessageProducer messageProducer = session.createProducer(destination);
		/**
		 * 设置投递方式 DeliveryMode.NON_PERSISTENT:不持久化
		 * 
		 * DeliveryMode.PERSISTENT:持久化
		 */
		messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

		for (int i = 0; i < 100; i++) {
			/**
			 * 创建出一条文本消息
			 */
			TextMessage textMessage = session.createTextMessage("我是消息：" + i);
			// 发送文本消息
			messageProducer.send(textMessage);
		}
		/**
		 * 消息提交
		 */
		session.commit();
		connection.close();

	}

}