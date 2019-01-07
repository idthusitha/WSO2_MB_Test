package org.sample.jms.topic;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.jms.TopicSubscriber;

public class Main {
	public static void main(String[] args) throws NamingException, JMSException {
		SampleTopicSubscriber topicSubscriber = new SampleTopicSubscriber();
		TopicSubscriber subscriber = topicSubscriber.subscribe();
		SampleTopicPublisher topicPublisher = new SampleTopicPublisher();
		topicPublisher.publishMessage("Test message 01");
		topicPublisher.publishMessage("Test message 02");
		topicSubscriber.receive(subscriber);
	}
}
