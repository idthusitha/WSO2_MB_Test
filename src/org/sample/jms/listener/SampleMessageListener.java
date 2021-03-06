package org.sample.jms.listener;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.jms.TopicConnection;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

public class SampleMessageListener implements javax.jms.MessageListener {
	private TopicConnection topicConnection;
	private TopicSession topicSession;
	private TopicSubscriber topicSubscriber;
	private QueueConnection queueConnection;
	private QueueSession queueSession;
	private MessageConsumer queueReceiver;
	private int count = 0;

	public SampleMessageListener(MessageConsumer queueReceiver, QueueSession queueSession, QueueConnection queueConnection) {
		this.queueReceiver = queueReceiver;
		this.queueSession = queueSession;
		this.queueConnection = queueConnection;
		System.out.println("Starting Queue Listener....");
	}

	public SampleMessageListener(TopicConnection topicConnection, TopicSession topicSession, TopicSubscriber topicSubscriber) {
		this.topicConnection = topicConnection;
		this.topicSession = topicSession;
		this.topicSubscriber = topicSubscriber;
		System.out.println("Starting Topic Listener....");
	}

	/**
	 * Override this method and add the operation which is needed to be done when a message is arrived
	 *
	 * @param message
	 *            - the next received message
	 */
	@Override
	public void onMessage(Message message) {
		count++;
		TextMessage receivedMessage = (TextMessage) message;
		try {
			System.out.println("Got the message ==> " + receivedMessage.getText());
			if (count >= 10) {
				closeAll(receivedMessage);
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private void closeAll(TextMessage receivedMessage) {
		try {
			if (receivedMessage.getText().contains("Queue")) {
				System.out.println("Closing Queue Listener...........");
				queueConnection.close();
				queueSession.close();
				queueReceiver.close();
			} else {
				System.out.println("Closing Topic Listener...........");
				topicConnection.close();
				topicSession.close();
				topicSubscriber.close();
			}
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
