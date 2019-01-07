package org.sample.jms.selector;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.naming.NamingException;

/**
 * A message selector is a String that contains an expression. The syntax of the expression is based on a subset of the SQL92 conditional expression syntax.
 * <p/>
 * JMS selectors work as filters
 */
public class Main {
	public static void main(String[] args) throws NamingException, JMSException {
		SampleQueueSelectorReceiver queueReceiver = new SampleQueueSelectorReceiver();
		// Message consumer with JMS Selector
		MessageConsumer consumer = queueReceiver.registerSubscriber("Currency ='LK' AND quantity < 3");
		SampleQueueSender queueSender = new SampleQueueSender();
		queueSender.sendMessages();
		queueReceiver.receiveMessages(consumer);
	}
}
