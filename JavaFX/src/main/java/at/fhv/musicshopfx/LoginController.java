package at.fhv.musicshopfx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javax.jms.JMSException;
import javax.security.auth.login.FailedLoginException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

public class LoginController {

    @FXML
    private Label loginFailedLabel;

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField serverTextField;

    @FXML
    private PasswordField passwordTextField;

    private SceneSwitcher sceneSwitcher = new SceneSwitcher();


    @FXML
    protected void login(ActionEvent e) throws IOException {
        try {
            if (SessionManager.login(usernameTextField.getText(), passwordTextField.getText(), serverTextField.getText())) {
                try {
                    MessageConsumerServiceImpl.getInstance();
                } catch (JMSException ex) {
                    ex.printStackTrace();
                }
                sceneSwitcher.switchSceneToMusicSearchView(e);
            }
        } catch (FailedLoginException | AccessDeniedException ex) {
            passwordTextField.clear();
            loginFailedLabel.setText(ex.getMessage());
        } catch (NotLoggedInException ex) {
            ex.printStackTrace();
        }
    }
}
