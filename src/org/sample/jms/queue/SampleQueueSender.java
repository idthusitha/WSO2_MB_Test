package org.sample.jms.queue;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class SampleQueueSender {
	public static final String QPID_ICF = "org.wso2.andes.jndi.PropertiesFileInitialContextFactory";
	private static final String CF_NAME_PREFIX = "connectionfactory.";
	private static final String QUEUE_NAME_PREFIX = "queue.";
	private static final String CF_NAME = "qpidConnectionfactory";
	String userName = "admin"; 
	String password = "admin";
	private static String CARBON_CLIENT_ID = "carbon";
	private static String CARBON_VIRTUAL_HOST_NAME = "carbon";
//	private static String CARBON_DEFAULT_HOSTNAME = "localhost";
//	private static String CARBON_DEFAULT_PORT = "5672";
	private static String CARBON_DEFAULT_HOSTNAME = "172.16.16.207";
	private static String CARBON_DEFAULT_PORT = "5672"; 
	String queueName = "testQueue";
	private QueueConnection queueConnection;
	private QueueSession queueSession;
//192.168.2.102:9444
	public void sendMessages(String message) throws NamingException, JMSException {
		Properties properties = new Properties();
		properties.put(Context.INITIAL_CONTEXT_FACTORY, QPID_ICF);
		properties.put(CF_NAME_PREFIX + CF_NAME, getTCPConnectionURL(userName, password));
		properties.put(QUEUE_NAME_PREFIX + queueName, queueName);
		InitialContext ctx = new InitialContext(properties);
		// Lookup connection factory
		QueueConnectionFactory connFactory = (QueueConnectionFactory) ctx.lookup(CF_NAME);
		queueConnection = connFactory.createQueueConnection();
		queueConnection.start();
		queueSession = queueConnection.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
		// Send message
		Queue queue = (Queue) ctx.lookup(queueName);
		// create the message to send
		TextMessage textMessage = queueSession.createTextMessage(message);
		javax.jms.QueueSender queueSender = queueSession.createSender(queue);
		queueSender.send(textMessage);
		queueSender.close();
		queueSession.close();
		queueConnection.close();
	}

	private String getTCPConnectionURL(String username, String password) {
		// amqp://{username}:{password}@carbon/carbon?brokerlist='tcp://{hostname}:{port}'
		return new StringBuffer().append("amqp://").append(username).append(":").append(password).append("@").append(CARBON_CLIENT_ID).append("/").append(CARBON_VIRTUAL_HOST_NAME).append("?brokerlist='tcp://").append(CARBON_DEFAULT_HOSTNAME)
				.append(":").append(CARBON_DEFAULT_PORT).append("'").toString();
	}

}
