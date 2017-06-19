package com.apacheMq.pubsub;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/***
 * 
 * �����ַ�ʽ Topic���ⷢ���Ͷ�����Ϣ ��ģʽ�� �����߱���һֱ�����Ųſ�������ʹ�ã����򷢲�����Ϣ���ղ���
 * 
 * @author WXW
 * 
 */
public class TopicReceiver {

	public static final String MQ_TCP_URL = "tcp://localhost:61616";
	public static final String TOPIC_DESTINATION = "com.chauvet.mq.topic";

	public static void main(String[] args) {

		TopicConnection connection = null;
		TopicSession session = null;
		try {
			TopicConnectionFactory factory = new ActiveMQConnectionFactory(ActiveMQConnection.DEFAULT_USER, ActiveMQConnection.DEFAULT_PASSWORD, MQ_TCP_URL);
			connection = factory.createTopicConnection();
			connection.start();
			session = connection.createTopicSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			Topic topic = session.createTopic(TOPIC_DESTINATION);
			TopicSubscriber subscriber = session.createSubscriber(topic);

			subscriber.setMessageListener(new MessageListener() {
				public void onMessage(Message msg) {
					if (msg != null) {
						MapMessage map = (MapMessage) msg;
						try {
							System.out.println(map.getLong("time") + "����#" + map.getString("text"));
						} catch (JMSException e) {
							e.printStackTrace();
						}
					}
				}
			});
			Thread.sleep(1000 * 60);
			session.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				try {
					session.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		}

	}
}