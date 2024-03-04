package at.fhv.musicshopfx;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.List;
import java.util.Map;

public interface MessageConsumerService {

    void addMessage(Message message, String topic);
    void removeMessage(Message message, String topic);
    List<Message> getMessagesFromSubscribedTopic(String topic);
    Map<String, List<Message>> getMessagesFromAllSubscribedTopics();
    void close() throws JMSException;

}
