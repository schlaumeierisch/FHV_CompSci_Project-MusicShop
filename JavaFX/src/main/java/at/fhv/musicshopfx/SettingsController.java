package at.fhv.musicshopfx;

import at.fhv.musicshopfx.domain.TopicLine;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import sharedrmi.communication.rmi.RMIController;
import sharedrmi.domain.valueobjects.Role;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class SettingsController {

    @FXML
    private Label usernameLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Label roleDescLabel;
    @FXML
    private Label topicErrorLabel;
    @FXML
    private ImageView cartIconImage;
    @FXML
    private ImageView invoiceIconImage;
    @FXML
    private ImageView messageIconImage;
    @FXML
    private ImageView settingsIconImage;
    @FXML
    private TableView<TopicLine> subscriptionView;
    @FXML
    private TableColumn<TopicLine, CheckBox> subscribedCol;
    @FXML
    private TableColumn<TopicLine, String> topicCol;
    @FXML
    private VBox navbarVbox;


    private RMIController rmiController;
    private String user;
    private List<Role> roles;
    private ObservableList<TopicLine> data;
    private final SceneSwitcher sceneSwitcher = new SceneSwitcher();
    private NavbarIconPositioner navbarIconPositioner = new NavbarIconPositioner();


    public void setData() throws RemoteException {

        try {
            this.rmiController = SessionManager.getInstance().getRMIController();
            this.roles = rmiController.getRoles();
            navbarIconPositioner.positionIcons(navbarVbox);

        } catch (NotLoggedInException | FileNotFoundException e) {
            e.printStackTrace();
        }

        this.user = this.rmiController.getUsername();
        this.usernameLabel.setText(this.user);
        this.roleLabel.setText(concatRoles(this.roles));

        if (this.roles.size() == 1)
            this.roleDescLabel.setText("Role");


        List<String> allTopics = this.rmiController.getAllTopics();
        List<String> subscribedTopics = this.rmiController.getSubscribedTopicsForUser(this.user);
        List<TopicLine> topicLinesForTableView = new ArrayList<>();

        for (String currentTopic : allTopics) {
            TopicLine topicLine = new TopicLine(currentTopic);

            if (subscribedTopics.contains(currentTopic)) {
                topicLine.getCheckbox().setSelected(true);
            }

            topicLinesForTableView.add(topicLine);
        }

        ObservableList<TopicLine> obsTopicLines = FXCollections.observableArrayList(topicLinesForTableView);

        topicCol.setCellValueFactory(new PropertyValueFactory<>("topicName"));
        subscribedCol.setCellValueFactory(new PropertyValueFactory<>("checkbox"));

        data = obsTopicLines;
        subscriptionView.setItems(data);
        subscriptionView.getSelectionModel().clearSelection();
    }

    private String concatRoles(List<Role> roles) {
        StringBuilder concatedRoles = new StringBuilder();

        for (int i = 0; i < roles.size(); i++) {
            concatedRoles.append(roles.get(i));

            if (i < roles.size()-1)
                concatedRoles.append(", ");
        }

        return concatedRoles.toString().toLowerCase();
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
    void applyButtonClicked() throws RemoteException {

        List<String> subscribedTopics = this.rmiController.getSubscribedTopicsForUser(this.user);

        for (TopicLine line : data)
        {
            String topic = line.getTopicName();
            boolean topicIsCurrentlySelected = line.getCheckbox().isSelected();

            if (!subscribedTopics.contains(topic) && topicIsCurrentlySelected) {
                this.rmiController.subscribe(topic, this.user);

            } else if (subscribedTopics.contains(topic) && !topicIsCurrentlySelected) {
                this.rmiController.unsubscribe(topic, this.user);
            }
        }
        topicErrorLabel.setText("Changes will be applied after logout!");
    }
}
