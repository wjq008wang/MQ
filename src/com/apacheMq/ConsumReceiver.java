package com.apacheMq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ConsumReceiver {

	public static void main(String[] args) throws JMSException {
		/**
		 * ActiveMQConnectionFactory:ActiveMQ的连接的工厂类
		 * 
		 * ActiveMQConnection.DEFAULT_USER:ActivieMQ默认的用户名
		 * ActiveMQConnection.DEFAULT_PASSWORD:ActiveMQ默认的密码
		 */
		// ConnectionFactory connectionFactory = new
		// ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER,
		// ActiveMQConnection.DEFAULT_PASSWORD, "tcp://127.0.0.1:61616");

		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("zhangsan",// ActiveMQConnectionFactory.DEFAULT_USER,
				"123",// ActiveMQConnectionFactory.DEFAULT_PASSWORD,
				"tcp://localhost:61616");
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
		MessageConsumer messageConsumer = session.createConsumer(destination);

		for (;;) {
			TextMessage textMessage = (TextMessage) messageConsumer.receive(1000);
			if (textMessage != null)
				System.out.println("receive: " + textMessage.getText());
			else
				break;
		}
		/**
		 * 消息提交
		 */
		session.close();
		connection.close();

	}

}
