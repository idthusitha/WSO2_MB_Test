package org.sample.jms.listener;

import javax.jms.JMSException;
import javax.naming.NamingException;

public class Main {
	public static void main(String[] args) throws NamingException, JMSException, InterruptedException {
		MessageListenerClient messageListenerClient = new MessageListenerClient();
		messageListenerClient.registerSubscribers();
	}
}
