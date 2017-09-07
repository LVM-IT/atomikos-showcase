package de.lvm.demo.atomikos.jms;

import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jms.AtomikosConnectionFactoryBean;
import java.util.Date;
import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.XAConnectionFactory;
import org.apache.activemq.ActiveMQXAConnectionFactory;
import org.junit.Test;

/**
 * Just to test that JMS with activemq works as expected with atomikos
 */
public class SimpleAtomikosJmsTest
{

    @Test
    public void sendMessage() throws Exception
    {
        // connect to local (docker) activemq
        final XAConnectionFactory xacf = new ActiveMQXAConnectionFactory("tcp://localhost:61616?jms.useAsyncSend=false");
        final AtomikosConnectionFactoryBean cf = new AtomikosConnectionFactoryBean();
        cf.setUniqueResourceName("activemq");
        cf.setXaConnectionFactory(xacf);
        cf.setPoolSize(1);

        
        final UserTransactionManager utm = new UserTransactionManager();
        utm.init();
        utm.begin();

        
        final Connection connection = cf.createConnection();
        connection.start();

        final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        final Destination destination = session.createQueue("TEST.SHOWCASE");
        final MessageProducer producer = session.createProducer(destination);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        final String text = "Sample message from thread: " + Thread.currentThread().getName() + " (atomikos) : " + new Date();
        final TextMessage message = session.createTextMessage(text);

        producer.send(message);
        producer.close();

        session.close();
        connection.close();
        
        utm.commit();
        utm.close();
    }
}
