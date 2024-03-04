package at.fhv.musicshopfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.IOException;
import java.rmi.RemoteException;

public class MessageController {

    @FXML
    private Label messageTopic;
    @FXML
    private Label messageTitle;
    @FXML
    private TextArea messageText;
    @FXML
    private Button deleteButton;
    @FXML
    private Button readMoreButton;

    private MessageConsumerService messageConsumerService = MessageConsumerServiceImpl.getInstance();
    private Message message;
    private String topic;
    private SceneSwitcher sceneSwitcher = new SceneSwitcher();

    public MessageController() throws RemoteException, NotLoggedInException, JMSException {
    }

    public void addMessage(Message message, String topic) throws IOException, JMSException {

        this.messageTopic.setText(topic);
        this.messageTitle.setText(message.getJMSCorrelationID());
        String messageText = ((TextMessage) message).getText();
        if(messageText.length() >= 250){
            this.messageText.setText(messageText.substring(0, 250)+"...");
        } else {
            this.messageText.setText(messageText);
        }
        this.message = message;
        this.topic = topic;

    }

    @FXML
    public void readMoreButtonOnClick(ActionEvent e) throws IOException {
        sceneSwitcher.switchSceneDetailedMessageView(e, message, topic);
    }

    @FXML
    public void deleteButtonOnClick(ActionEvent e) throws IOException {
        messageConsumerService.removeMessage(message, topic);
        sceneSwitcher.switchSceneToMessageBoardView(e);

    }
}
