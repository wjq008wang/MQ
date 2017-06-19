package com.apacheMq.db;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Sender {

	public static void main(String[] args) throws Exception {

		// 1������ConnectionFactory����������Ҫ�����û��������룬�Լ����ӵĵ�ַ
		// ��ʹ��Ĭ�ϡ��˿ں�Ϊ"tcp://localhost:61616"
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("zhangsan",// ActiveMQConnectionFactory.DEFAULT_USER,
				"123",// ActiveMQConnectionFactory.DEFAULT_PASSWORD,
				"tcp://localhost:61616");
		// 2��ͨ��ConnectionFactory�������󴴽�һ��Connection����
		// ���ҵ���Connection��start������������,ConnectionĬ���ǲ�������
		Connection connection = connectionFactory.createConnection();
		connection.start();

		// 3��ͨ��Connection���󴴽�Session�Ự�������Ļ������󣩣�
		// ����һ����ʾ�Ƿ�������
		// ����������ʾ����ǩ��ģʽ��һ��ʹ�õ����Զ�ǩ�պͿͻ����Լ�ȷ��ǩ��

		// ��һ����������Ϊtrue����ʾ��������
		// ��������󣬼ǵ�Ҫ�ֶ��ύ����

		Session session = connection.createSession(Boolean.TRUE, Session.CLIENT_ACKNOWLEDGE);

		// 4��ͨ��Session����Destination����ָ����һ���ͻ�������ָ��������ϢĿ���������Ϣ��Դ�Ķ���
		// ��PTPģʽ��,Destinationָ����Queue
		// �ڷ�������ģʽ�У�Destinationָ����Topic
		Destination destination = session.createQueue("queue1");

		// 5��ʹ��Session��������Ϣ����������߻���������
		MessageProducer messageProducer = session.createProducer(destination);

		// 6������ǣ������ߣ�ʹ��MessageProducer��setDeliverMode�������ã���Ϣ�ĳ־û��ͷǳ־û�
		messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
		// 7�����ʹ��JMS�淶��TextMessage��ʽ��������(ͨ��Session����)
		// ������MessageProducer��send������������
		for (int i = 0; i < 5; i++) {
			TextMessage textMessage = session.createTextMessage();
			textMessage.setText("������Ϣ" + i);
			messageProducer.send(textMessage);
		}

		// �ֶ��ύ����������
		session.commit();

		// �ͷ�����
		if (connection != null) {
			connection.close();
		}
	}
}
