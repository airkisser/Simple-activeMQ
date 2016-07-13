package com.airkisser.mq.queue;

import java.util.Date;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * @author AIR
 *
 */
public class Producer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String user = ActiveMQConnection.DEFAULT_USER;
		String password = ActiveMQConnection.DEFAULT_PASSWORD;
		String url = ActiveMQConnection.DEFAULT_BROKER_URL;
		String subject = "TestQueue";
		// 创建连接工厂
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user, password, url);
		try {
			// 创建连接
			Connection connection = connectionFactory.createConnection();
			connection.start();
			/*
			 * 创建会话 在非事务性会话中，消息何时被确认取决于创建会话时的应答模式（acknowledgement
			 * mode）。该参数有以下三个可选值：
			 * Session.AUTO_ACKNOWLEDGE。当客户成功的从receive方法返回的时候，或者从MessageListener
			 * .onMessage方法成功返回的时候，会话自动确认客户收到的消息。
			 * Session.CLIENT_ACKNOWLEDGE。客户通过消息的acknowledge方法确认消息。
			 * 需要注意的是，在这种模式中，确认是在会话层上进行：确认一个被消费的消息将自动确认所有已被会话消费的消息。
			 * 例如，如果一个消息消费者消费了10个消息，然后确认第5个消息，那么所有10个消息都被确认。
			 * Session.DUPS_ACKNOWLEDGE。该选择只是会话迟钝的确认消息的提交。 如果JMS
			 * Provider失败，那么可能会导致一些重复的消息。 如果是重复的消息，那么JMS
			 * Provider必须把消息头的JMSRedelivered字段设置为true。
			 */
			Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
			Destination destination = session.createQueue(subject);
			MessageProducer producer = session.createProducer(destination);
			for (int i = 0; i < 20; i++) {
				MapMessage message = session.createMapMessage();
				Date date = new Date();
				message.setLong("count", date.getTime());
				Thread.sleep(1000L);
				producer.send(message);
				System.out.println("---发送消息:"+date);
			}
			session.commit();
			session.close();
			connection.close();
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

}
