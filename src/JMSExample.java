import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

public class JMSExample {

	public static void main(String[] args) {
		JMSExample producer = new JMSExample();
		producer.runTest();
	}

	private void runTest() {
		try {
			Properties properties = new Properties();
			//properties.put("connectionfactory.qpidConnectionfactory", "amqp://admin:admin@carbon/carbon?brokerlist='tcp://localhost:5672'");
			properties.put("connectionfactory.qpidConnectionfactory", "amqp://admin:admin@carbon/carbon?brokerlist='https://192.168.2.102:9444'");
			
			
			properties.put("destination.samplequeue", "samplequeue; {create:always}");

			properties.put("java.naming.factory.initial", "org.apache.qpid.jndi.PropertiesFileInitialContextFactory");
			Context context = new InitialContext(properties);

			ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("qpidConnectionfactory");
			Connection connection = connectionFactory.createConnection();
			connection.start();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue destination = (Queue) context.lookup("samplequeue");

			MessageProducer producer = session.createProducer(destination);
			TextMessage outMessage = session.createTextMessage();
			outMessage.setText("Hello World!");
			producer.send(outMessage);

			MessageConsumer consumer = session.createConsumer(destination);
			Message inMessage = consumer.receive();
			System.out.println(((TextMessage) inMessage).getText());

			connection.close();
			context.close();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}
}
