package at.fhv.musicshopfx;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import sharedrmi.application.dto.CartLineItemDTO;
import sharedrmi.application.dto.CustomerDTO;
import sharedrmi.application.dto.InvoiceDTO;
import sharedrmi.application.dto.InvoiceLineItemDTO;
import sharedrmi.application.exceptions.AlbumNotFoundException;
import sharedrmi.application.exceptions.NotEnoughStockException;
import sharedrmi.communication.rmi.RMIController;
import sharedrmi.domain.enums.PaymentMethod;
import sharedrmi.domain.valueobjects.Address;
import sharedrmi.domain.valueobjects.CustomerData;
import sharedrmi.domain.valueobjects.InvoiceId;
import sharedrmi.domain.valueobjects.Role;

import javax.naming.NoPermissionException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CheckoutController {

    @FXML
    private RadioButton anonymousCustomerRadioButton;
    @FXML
    private RadioButton existingCustomerRadioButton;
    @FXML
    private RadioButton cashRadioButton;
    @FXML
    private RadioButton creditCardRadioButton;
    @FXML
    private Button checkoutButton;
    @FXML
    private TableView customerTableView;
    @FXML
    private Button searchButton;
    @FXML
    private TextField customerSearchTextField;
    @FXML
    private TableColumn firstNameCol;
    @FXML
    private TableColumn familyNameCol;
    @FXML
    private TableColumn emailAddressCol;
    @FXML
    private ToggleGroup paymentMethod;
    @FXML
    private Label checkoutErrorLabel;
    @FXML
    private ToggleGroup customerSettingsToggleGroup;
    @FXML
    private ImageView cartIconImage;
    @FXML
    private ImageView invoiceIconImage;
    @FXML
    private ImageView messageIconImage;
    @FXML
    private ImageView settingsIconImage;
    @FXML
    private VBox navbarVbox;

    private SceneSwitcher sceneSwitcher = new SceneSwitcher();
    private RMIController rmiController;
    private List<Role> roles;
    private List<CartLineItemDTO> cartLineItemDTOs;
    private NavbarIconPositioner navbarIconPositioner = new NavbarIconPositioner();


    public void setData(List<CartLineItemDTO> cartLineItemDTOs) throws IOException {
        try {
            this.rmiController = SessionManager.getInstance().getRMIController();
            this.roles = rmiController.getRoles();
            navbarIconPositioner.positionIcons(navbarVbox);

        } catch (NotLoggedInException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        customerSettingsToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> changed,
                                Toggle oldVal, Toggle newVal) {
                switch (((RadioButton) customerSettingsToggleGroup.getSelectedToggle()).getText()){
                    case "Anonymous Customer":
                        searchButton.setDisable(true);
                        customerSearchTextField.setDisable(true);
                        customerTableView.setDisable(true);
                        checkoutErrorLabel.setText("");
                        break;

                    case "Existing Customer":
                        searchButton.setDisable(false);
                        customerSearchTextField.setDisable(false);
                        customerTableView.setDisable(false);
                }
            }
        });

        this.cartLineItemDTOs = cartLineItemDTOs;
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

    @FXML
    protected void customerSearchButtonClicked() {
        try {

            List<CustomerDTO> customers = rmiController.findCustomersByName(customerSearchTextField.getText());

            ObservableList<CustomerDTO> customerDTOs = FXCollections.observableArrayList(customers);

            firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
            familyNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
            emailAddressCol.setCellValueFactory(new PropertyValueFactory<>("email"));
            customerTableView.setItems(customerDTOs);

        } catch (NoPermissionException | RemoteException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void checkoutButtonClicked(ActionEvent event) throws NoPermissionException {
        SessionManager.setLastAlbums(new ArrayList<>());
        SessionManager.setLastSearch("");

        PaymentMethod selectedPaymentMethod = PaymentMethod.CASH;

        switch (((RadioButton) paymentMethod.getSelectedToggle()).getText()) {
            case "Cash":
                selectedPaymentMethod = PaymentMethod.CASH;
                break;

            case "Credit Card":
                selectedPaymentMethod = PaymentMethod.CREDIT_CARD;
                break;
        }

        CustomerDTO customerDTO = (CustomerDTO) customerTableView.getSelectionModel().getSelectedItem();
        String customerSettingsString = ((RadioButton) customerSettingsToggleGroup.getSelectedToggle()).getText();

        if (customerDTO != null || customerSettingsString.equals("Anonymous Customer")) {
            InvoiceDTO invoiceDTO = null;

            switch (customerSettingsString) {
                case "Anonymous Customer":
                    invoiceDTO = InvoiceDTO.builder()
                            .invoiceId(new InvoiceId())
                            .date(LocalDate.now())
                            .paymentMethod(selectedPaymentMethod)
                            .invoiceLineItems(InvoiceLineItemDTO.createFromCartLineItemDTOs(cartLineItemDTOs))
                            .build();
                    break;

                case "Existing Customer":
                    invoiceDTO = InvoiceDTO.builder()
                            .invoiceId(new InvoiceId())
                            .date(LocalDate.now())
                            .paymentMethod(selectedPaymentMethod)
                            .invoiceLineItems(InvoiceLineItemDTO.createFromCartLineItemDTOs(cartLineItemDTOs))
                            .customerData(
                                    CustomerData.builder()
                                            .firstName(customerDTO.getFirstName())
                                            .lastName(customerDTO.getLastName())
                                            .email(customerDTO.getEmail())
                                            .address(Address.builder()
                                                    .addressCountry(customerDTO.getAddress().getAddressCountry())
                                                    .addressLocality(customerDTO.getAddress().getAddressLocality())
                                                    .postalCode(customerDTO.getAddress().getPostalCode())
                                                    .streetAddress(customerDTO.getAddress().getStreetAddress())
                                                    .build())
                                            .build()
                            )
                            .build();
            }

        try {
            InvoiceId invoiceId = rmiController.createInvoice(invoiceDTO);
            rmiController.clearCart();

            TextArea textArea = new TextArea("Invoice ID: " + invoiceId.getInvoiceId());
            textArea.setEditable(false);
            textArea.setWrapText(true);
            GridPane gridPane = new GridPane();
            gridPane.setMaxWidth(Double.MAX_VALUE);
            gridPane.add(textArea, 0, 0);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Checkout Success");
            alert.setHeaderText("Items successfully bought and invoice created!");
            alert.getDialogPane().setContent(gridPane);

            alert.showAndWait();

            sceneSwitcher.switchSceneToMusicSearchView(event);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (AlbumNotFoundException | NotEnoughStockException e) {
                checkoutErrorLabel.setText(e.getMessage());
            }
        } else {
            checkoutErrorLabel.setText("select an existing customer or choose anonymous customer");
        }
    }
}
