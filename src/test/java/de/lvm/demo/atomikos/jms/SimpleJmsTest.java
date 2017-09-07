package de.lvm.demo.atomikos.jms;

import java.util.Date;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

/**
 * Just to test that JMS with activemq works as expected
 */
public class SimpleJmsTest
{

    @Test
    public void sendUntransactedMessage() throws Exception
    {
        // connect to local (docker) activemq
        final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616?jms.useAsyncSend=false");

        final Connection connection = connectionFactory.createConnection();
        connection.start();

        final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        final Destination destination = session.createQueue("TEST.SHOWCASE");
        final MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        final String text = "Sample message from thread: " + Thread.currentThread().getName() + " : " + new Date();
        final TextMessage message = session.createTextMessage(text);

        producer.send(message);

        session.close();
        connection.close();
    }
    
    @Test
    public void sendTransactedMessage() throws Exception
    {
        // connect to local (docker) activemq
        final ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616?jms.useAsyncSend=false");

        final Connection connection = connectionFactory.createConnection();
        connection.start();

        final Session session = connection.createSession(true, Session.SESSION_TRANSACTED);

        final Destination destination = session.createQueue("TEST.SHOWCASE");
        final MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        final String text = "Sample message from thread: " + Thread.currentThread().getName() + " : " + new Date() + " (transacted)";
        final TextMessage message = session.createTextMessage(text);

        producer.send(message);
        producer.close();
        
        session.commit();
        session.close();
        
        connection.close();
    }
}
