package org.sample.jms.cluster;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class SendClient {
	//first wso2 mb instance
	//./wso2server.sh

	//second wso2 mb instance
	//./wso2server.sh -DportOffset=1

	public static void main(String[] args) {
		SendClient sendClient = new SendClient();
		sendClient.sendMessage();
	}

	public void sendMessage() {
		Properties initialContextProperties = new Properties();
		initialContextProperties.put("java.naming.factory.initial", "org.wso2.andes.jndi.PropertiesFileInitialContextFactory");
		String connectionString = "amqp://admin:admin@clientID/carbon?brokerlist='tcp://localhost:5673'";
		initialContextProperties.put("connectionfactory.qpidConnectionfactory", connectionString);
		initialContextProperties.put("queue.myQueue", "myQueue");

		try {
			InitialContext initialContext = new InitialContext(initialContextProperties);
			QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) initialContext.lookup("qpidConnectionfactory");
			QueueConnection queueConnection = queueConnectionFactory.createQueueConnection();
			queueConnection.start();
			QueueSession queueSession = queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			TextMessage textMessage = queueSession.createTextMessage();
			textMessage.setText("Test message");
			System.out.println("Sending Message : " + textMessage.getText().length());
			// Send message
			Queue queue = (Queue) initialContext.lookup("myQueue");
			QueueSender queueSender = queueSession.createSender(queue);
			queueSender.send(textMessage);
			// Housekeeping
			queueSender.close();
			queueSession.close();
			queueConnection.stop();
			queueConnection.close();
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
