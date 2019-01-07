package org.sample.jms.queue;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.naming.NamingException;

//https://docs.wso2.com/display/MB300/Sending+and+Receiving+Messages+Using+Queues

public class Main {
	public static void main(String[] args) throws NamingException, JMSException {

		SampleQueueReceiver queueReceiver = new SampleQueueReceiver();
		MessageConsumer consumer = queueReceiver.registerSubscriber();
		SampleQueueSender queueSender = new SampleQueueSender();
		queueSender.sendMessages("aaaaaaaaaaaaaaaaaaaa");
		queueReceiver.receiveMessages(consumer);
	}
}
