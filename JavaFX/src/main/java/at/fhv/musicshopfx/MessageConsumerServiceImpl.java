package at.fhv.musicshopfx;


import sharedrmi.communication.rmi.RMIController;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.*;

public class MessageConsumerServiceImpl implements MessageConsumerService {

    private static MessageConsumerService instance;
    private List<TopicConsumer> topicConsumers;
    private SessionManager sessionManager;
    private RMIController rmiController;
    private static Map<String, List<Message>> topicMessages;

    private MessageConsumerServiceImpl() throws NotLoggedInException, JMSException {
        this.sessionManager = SessionManager.getInstance();
        this.topicConsumers = new LinkedList<>();
        this.topicMessages = new HashMap<>();
        rmiController = sessionManager.getRMIController();


        String username = sessionManager.getLoggedInUsername();
        List<String> topics = rmiController.getSubscribedTopicsForUser(username);

        for (String topic:topics) {
            TopicConsumer consumer = new TopicConsumer(topic, username);
            topicConsumers.add(consumer);
        }

    }

    public static MessageConsumerService getInstance() throws NotLoggedInException, JMSException {
        if(instance == null){
            instance = new MessageConsumerServiceImpl();
        }

        return instance;
    }

    public void close() throws JMSException {

        for (TopicConsumer topicConsumer:topicConsumers) {
            topicConsumer.close();
        }
        instance = null;
    }

    @Override
    public Map<String, List<Message>> getMessagesFromAllSubscribedTopics(){
        return topicMessages;
    }

    public List<Message> getMessagesFromSubscribedTopic(String topic){
        List<Message> messages = topicMessages.get(topic);
        if (messages != null) {
            return messages;
        }

        return Collections.emptyList();
    }

    @Override
    public void addMessage(Message message, String topic){
        List<Message> messages = topicMessages.get(topic);
        if(messages == null){
            messages = new LinkedList<>();
        }
        messages.add(message);
        topicMessages.put(topic, messages);

    }

    @Override
    public void removeMessage(Message message, String topic){
        List<Message> messages = topicMessages.get(topic);
        messages.remove(message);
        try {
            message.acknowledge();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
