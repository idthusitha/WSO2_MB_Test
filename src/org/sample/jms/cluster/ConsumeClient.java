package org.sample.jms.cluster;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class ConsumeClient {
	public void consumeMessage() {
		Properties initialContextProperties = new Properties();
		initialContextProperties.put("java.naming.factory.initial", "org.wso2.andes.jndi.PropertiesFileInitialContextFactory");
		String connectionString = "amqp://admin:admin@clientID/carbon?brokerlist='tcp://localhost:5672'";
		initialContextProperties.put("connectionfactory.qpidConnectionfactory", connectionString);
		initialContextProperties.put("queue.myQueue", "myQueue");
		try {
			InitialContext initialContext = new InitialContext(initialContextProperties);
			QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) initialContext.lookup("qpidConnectionfactory");
			QueueConnection queueConnection = queueConnectionFactory.createQueueConnection();
			queueConnection.start();
			QueueSession queueSession = queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			Destination destination = (Destination) initialContext.lookup("myQueue");
			MessageConsumer messageConsumer = queueSession.createConsumer(destination);
			TextMessage textMessage = (TextMessage) messageConsumer.receive();
			System.out.println("Got message ==> " + textMessage.getText());
			try {
				Thread.sleep(9000);
			} catch (Exception e) {
				System.out.println(e);
			}
			messageConsumer.close();
			queueSession.close();
			queueConnection.stop();
			queueConnection.close();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ConsumeClient sendConsumeClient = new ConsumeClient();
		sendConsumeClient.consumeMessage();
	}
}
