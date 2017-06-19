package com.apacheMq.pubsub;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Publisher {

	// ����ģʽ

	// 1�����ӹ���
	private ConnectionFactory connectionFactory;
	// 2�����Ӷ���
	private Connection connection;
	// 3��Session����
	private Session session;
	// 4��������
	private MessageProducer messageProducer;

	public Publisher() {

		try {
			this.connectionFactory = new ActiveMQConnectionFactory("zhangsan", "123", "tcp://localhost:61616");
			this.connection = connectionFactory.createConnection();
			this.connection.start();
			// ��ʹ������
			// ���ÿͻ���ǩ��ģʽ
			this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			this.messageProducer = this.session.createProducer(null);
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}

	}

	public Session getSession() {
		return this.session;
	}

	public void send1(/* String QueueName, Message message */) {
		try {

			Destination destination = this.session.createTopic("topic1");
			MapMessage msg1 = this.session.createMapMessage();
			msg1.setString("name", "����");
			msg1.setInt("age", 22);

			MapMessage msg2 = this.session.createMapMessage();
			msg2.setString("name", "����");
			msg2.setInt("age", 25);

			MapMessage msg3 = this.session.createMapMessage();
			msg3.setString("name", "����");
			msg3.setInt("age", 30);

			// ������Ϣ��topic1
			this.messageProducer.send(destination, msg1, DeliveryMode.NON_PERSISTENT, 4, 1000 * 60 * 10);
			this.messageProducer.send(destination, msg2, DeliveryMode.NON_PERSISTENT, 4, 1000 * 60 * 10);
			this.messageProducer.send(destination, msg3, DeliveryMode.NON_PERSISTENT, 4, 1000 * 60 * 10);

		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}

	public void send2() {
		try {
			Destination destination = this.session.createTopic("topic1");
			TextMessage message = this.session.createTextMessage("����һ���ַ���");
			// ������Ϣ
			this.messageProducer.send(destination, message, DeliveryMode.NON_PERSISTENT, 4, 1000 * 60 * 10);
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}

	}

	public static void main(String[] args) {
		Publisher producer = new Publisher();
		producer.send1();

	}

}