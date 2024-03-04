package at.fhv.musicshopfx;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import sharedrmi.application.exceptions.UserNotFoundException;
import sharedrmi.communication.rmi.RMIController;

import javax.jms.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class TopicConsumer implements MessageListener
{
    // Connection Factory for connection to the ActiveMQ server
    private final ActiveMQConnectionFactory connectionFactory;

    // Parameters
    private final String topicToConsume;
    private final String clientid;
    private Connection connection;
    private Session session;
    private RMIController rmiController = SessionManager.getInstance().getRMIController();

    public TopicConsumer(String topicToConsume, String clientId) throws NotLoggedInException, JMSException {
        this.connectionFactory = new ActiveMQConnectionFactory("tcp://10.0.40.162:61616");
        this.topicToConsume = topicToConsume;
        this.clientid = clientId+topicToConsume;
        // Create a Connection
        connection = connectionFactory.createConnection();
        connection.setClientID(this.clientid);
        connection.start();

        // Create a Session
        session = connection.createSession(false, ActiveMQSession.INDIVIDUAL_ACKNOWLEDGE);

        // Create a Topic. If the Topic already exists, it will be returned.
        Destination destination = session.createTopic(this.topicToConsume);

        // Create Durable Subscriber
        Topic topic = (Topic) destination;
        TopicSubscriber topicSubscriber = session.createDurableSubscriber(topic, "Message Subscription");

        // Use MessageListener in order to consume messages (including past messages)
        // non-blocking (implement onMessage())
        topicSubscriber.setMessageListener(this);

    }

    public void close() throws JMSException {
        session.close();
        connection.close();
    }

    @Override
    public void onMessage(Message message)
    {
        MessageConsumerService messageConsumerService = null;
        try {
            messageConsumerService = MessageConsumerServiceImpl.getInstance();
        } catch ( NotLoggedInException | JMSException e) {
            e.printStackTrace();
        }
        System.out.println("MESSAGE RECEIVED");
        try {
            LocalDateTime messageTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(message.getJMSTimestamp()), TimeZone.getDefault().toZoneId());
            LocalDateTime lastViewedTime = rmiController.getLastViewedForUser(SessionManager.getLoggedInUsername());
            if(messageTime.isAfter(lastViewedTime)){
                SessionManager.setNewMessages(true);
            }
            messageConsumerService.addMessage(message, topicToConsume);
        } catch (JMSException | UserNotFoundException e) {
            e.printStackTrace();
        }

    }
}