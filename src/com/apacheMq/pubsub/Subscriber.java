package com.apacheMq.pubsub;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Subscriber {

	// ����ģʽ

	// 1�����ӹ���
	private ConnectionFactory connectionFactory;
	// 2�����Ӷ���
	private Connection connection;
	// 3��Session����
	private Session session;
	// 4��������
	private MessageConsumer messageConsumer;
	// 5��Ŀ�ĵ�ַ
	private Destination destination;

	public Subscriber() {

		try {
			this.connectionFactory = new ActiveMQConnectionFactory("zhangsan", "123", "tcp://localhost:61616");
			this.connection = connectionFactory.createConnection();
			this.connection.start();
			// ��ʹ������
			// ���ÿͻ���ǩ��ģʽ
			this.session = this.connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			this.destination = this.session.createTopic("topic1");
			this.messageConsumer = this.session.createConsumer(destination);
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}

	}

	public Session getSession() {
		return this.session;
	}

	// ���ڼ�����Ϣ���е���Ϣ
	class MyLister implements MessageListener {

		@Override
		public void onMessage(Message message) {
			try {
				if (message instanceof TextMessage) {

				}
				if (message instanceof MapMessage) {
					MapMessage ret = (MapMessage) message;
					System.out.println(ret.toString());
					System.out.println(ret.getString("name"));
					System.out.println(ret.getInt("age"));
					// ��Ϊ���õ��ǿͻ��˵�ǩ��ģʽ������Ҫ�ֶ���ȥȷ����Ϣ������
					message.acknowledge();
				}
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}
		}

	}

	// �����첽������Ϣ
	public void receiver() {
		try {
			this.messageConsumer.setMessageListener(new MyLister());
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		Subscriber conmuser = new Subscriber();
		conmuser.receiver();

	}

}