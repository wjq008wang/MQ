package com.apacheMq.pubsub;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/***
 * 
 * 第三种方式 Topic 主题发布 和 订阅消息 该模式下 订阅者必须一直开启着才可以正常使用，否则发布的消息接收不到
 * 
 * @author WXW
 * 
 */
public class TopicSender {

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
			TopicPublisher publisher = session.createPublisher(topic);
			publisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
			sendMessage(session, publisher);
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

	public static void sendMessage(TopicSession session, TopicPublisher publisher) throws Exception {
		System.out.println("------------发送消息开始-----------");
		MapMessage map = session.createMapMessage();
		map.setString("text", "【this is chauvet's TOPIC message】");
		map.setLong("time", System.currentTimeMillis());
		publisher.send(map);
		System.out.println("------------发送消息结束-----------");
	}
}
