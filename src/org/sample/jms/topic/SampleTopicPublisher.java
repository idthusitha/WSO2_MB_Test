package org.sample.jms.topic;

import javax.jms.JMSException;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class SampleTopicPublisher {
	public static final String QPID_ICF = "org.wso2.andes.jndi.PropertiesFileInitialContextFactory";
	private static final String CF_NAME_PREFIX = "connectionfactory.";
	private static final String CF_NAME = "qpidConnectionfactory";
	String userName = "admin";
	String password = "admin";
	private static String CARBON_CLIENT_ID = "carbon";
	private static String CARBON_VIRTUAL_HOST_NAME = "carbon";
	//private static String CARBON_DEFAULT_HOSTNAME = "localhost";
	private static String CARBON_DEFAULT_HOSTNAME = "172.16.16.207";
	private static String CARBON_DEFAULT_PORT = "5672";
	String topicName = "MYTopic";

	public void publishMessage(String message) throws NamingException, JMSException {
		Properties properties = new Properties();
		properties.put(Context.INITIAL_CONTEXT_FACTORY, QPID_ICF);
		properties.put(CF_NAME_PREFIX + CF_NAME, getTCPConnectionURL(userName, password));
		System.out.println("getTCPConnectionURL(userName,password) = " + getTCPConnectionURL(userName, password));
		InitialContext ctx = new InitialContext(properties);
		
		
		// Lookup connection factory
		TopicConnectionFactory connFactory = (TopicConnectionFactory) ctx.lookup(CF_NAME);
		TopicConnection topicConnection = connFactory.createTopicConnection();
		topicConnection.start();
		
		
		TopicSession topicSession = topicConnection.createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE);
		
		
		
		// Send message
		Topic topic = topicSession.createTopic(topicName);
		
		
		
		// create the message to send
		TextMessage textMessage = topicSession.createTextMessage(message);
		javax.jms.TopicPublisher topicPublisher = topicSession.createPublisher(topic);
		topicPublisher.publish(textMessage);
		topicPublisher.close();
		topicSession.close();
		topicConnection.stop();
		topicConnection.close();
	}

	private String getTCPConnectionURL(String username, String password) {
		// amqp://{username}:{password}@carbon/carbon?brokerlist='tcp://{hostname}:{port}'
		return new StringBuffer().append("amqp://").append(username).append(":").append(password).append("@").append(CARBON_CLIENT_ID).append("/").append(CARBON_VIRTUAL_HOST_NAME).append("?brokerlist='tcp://").append(CARBON_DEFAULT_HOSTNAME)
				.append(":").append(CARBON_DEFAULT_PORT).append("'").toString();
	}
}
