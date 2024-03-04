package at.fhv.musicshopfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.TimeZone;

public class DetailedMessageController {
    private final SceneSwitcher sceneSwitcher = new SceneSwitcher();
    @FXML
    private ImageView cartIconImage;
    @FXML
    private ImageView invoiceIconImage;
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private ImageView messageBoardIconImage;
    @FXML
    private Label messageDateLabel;
    @FXML
    private Label messageErrorLabel;
    @FXML
    private Label messageExpirationDateLabel;
    @FXML
    private ImageView messageIconImage;
    @FXML
    private TextArea messageTextArea;
    @FXML
    private Label messageTitleLabel;
    @FXML
    private Label messageTopicLabel;
    @FXML
    private ImageView musicIconImage;
    @FXML
    private VBox navbarVbox;

    private MessageConsumerService messageConsumerService = MessageConsumerServiceImpl.getInstance();

    private Message message;
    private String topic;
    private NavbarIconPositioner navbarIconPositioner = new NavbarIconPositioner();

    public DetailedMessageController() throws JMSException, RemoteException, NotLoggedInException {
    }

    public void setData(Message message, String topic) {

        this.message = message;
        this.topic = topic;

        try {
            LocalDateTime messageTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(message.getJMSTimestamp()), TimeZone.getDefault().toZoneId());
            LocalDateTime expirationDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(message.getJMSExpiration()), TimeZone.getDefault().toZoneId());

            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

            messageTitleLabel.setText(message.getJMSCorrelationID());
            messageDateLabel.setText(dateTimeFormatter.format(messageTime));
            messageExpirationDateLabel.setText(dateTimeFormatter.format(expirationDate));
            messageTopicLabel.setText(topic);
            messageTextArea.setText(((TextMessage) message).getText());

            navbarIconPositioner.positionIcons(navbarVbox);

        } catch (JMSException | FileNotFoundException e) {
            e.printStackTrace();
        }

        if(SessionManager.isNewMessageAvailable()){
            messageBoardIconImage.setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/envelopered.png"))));
        }
    }


    @FXML
    public void deleteButtonOnClick(ActionEvent e) throws IOException {
        messageConsumerService.removeMessage(message, topic);
        sceneSwitcher.switchSceneToMessageBoardView(e);

    }



    @FXML
    protected void searchSymbolClicked(MouseEvent e) throws IOException {
        if (e.isPrimaryButtonDown()) {
            sceneSwitcher.switchSceneToMusicSearchView(e);
        }
    }

    @FXML
    protected void cartSymbolClicked(MouseEvent e) throws IOException {
        if (e.isPrimaryButtonDown()) {
            sceneSwitcher.switchSceneToCartView(e);
        }
    }

    @FXML
    protected void invoiceSymbolClicked(MouseEvent e) throws IOException {
        if (e.isPrimaryButtonDown()) {
            sceneSwitcher.switchSceneToInvoiceSearchView(e);
        }
    }

    @FXML
    protected void messageSymbolClicked(MouseEvent e) throws IOException {
        if (e.isPrimaryButtonDown()) {
            sceneSwitcher.switchSceneToMessageProducerView(e);
        }
    }

    @FXML
    protected void logoutSymbolClicked(MouseEvent e) throws IOException {
        if (e.isPrimaryButtonDown()) {
            try {
                SessionManager.logout();
            } catch (NotLoggedInException ex) {
                ex.printStackTrace();
                return;
            }

            sceneSwitcher.switchSceneToLoginView(e);
        }
    }

    @FXML
    protected void messageBoardSymbolClicked(MouseEvent e) throws IOException {
        if (e.isPrimaryButtonDown()) {
            sceneSwitcher.switchSceneToMessageBoardView(e);
        }
    }
}
