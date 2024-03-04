package at.fhv.musicshopfx;

import at.fhv.musicshopfx.domain.CartLineItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import sharedrmi.application.dto.AlbumDTO;
import sharedrmi.application.dto.CartLineItemDTO;
import sharedrmi.application.exceptions.AlbumNotFoundException;
import sharedrmi.communication.rmi.RMIController;
import sharedrmi.domain.valueobjects.Role;

import javax.naming.NoPermissionException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CartController {

    @FXML
    private TableView<CartLineItem> cartView;
    @FXML
    private TableColumn<CartLineItem, String> productCol;
    @FXML
    private TableColumn<CartLineItem, String> mediumTypeCol;
    @FXML
    private TableColumn<CartLineItem, String> stockCol;
    @FXML
    private TableColumn<CartLineItem, String> quantityCol;
    @FXML
    private TableColumn<CartLineItem, String> priceCol;
    @FXML
    private TableColumn<CartLineItem, String> minusCol;
    @FXML
    private TableColumn<CartLineItem, String> plusCol;
    @FXML
    private TableColumn<CartLineItem, String> xCol;
    @FXML
    private Label totalPriceLabel;
    @FXML
    private Button buyButton;
    @FXML
    private Button clearCartButton;
    @FXML
    private VBox navbarVbox;

    private ObservableList<CartLineItem> data;
    private List<CartLineItemDTO> cartLineItemDTOs;

    private final int PRODUCT_COLUMN_POSITION = 0;
    private final int MINUS_COLUMN_POSITION = 3;
    private final int PLUS_COLUMN_POSITION = 5;
    private final int CROSS_COLUMN_POSITION = 7;

    private final String CURRENCY = "€";
    private RMIController rmiController;
    private List<Role> roles;
    private NavbarIconPositioner navbarIconPositioner = new NavbarIconPositioner();

    private SceneSwitcher sceneSwitcher = new SceneSwitcher();

    public void setData() throws IOException, NoPermissionException {

        try {
            this.rmiController = SessionManager.getInstance().getRMIController();
            this.roles = rmiController.getRoles();
            navbarIconPositioner.positionIcons(navbarVbox);

        } catch (NotLoggedInException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        // translate List<LineItemDTO> to List<CartLineItem>
        List<CartLineItem> cartLineItems = new ArrayList<>();


        cartLineItemDTOs = rmiController.getCart().getCartLineItems();
        for (CartLineItemDTO cartLineItemDTO : cartLineItemDTOs) {
            cartLineItems.add(new CartLineItem(cartLineItemDTO.getName(),
                    cartLineItemDTO.getMediumType(),
                    cartLineItemDTO.getStock(),
                    cartLineItemDTO.getQuantity(),
                    cartLineItemDTO.getPrice(),
                    "-",
                    "+",
                    "x", cartLineItemDTO
            ));
        }

        // prepare UI table
        ObservableList<CartLineItem> obsDTOs = FXCollections.observableArrayList(cartLineItems);

        productCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mediumTypeCol.setCellValueFactory(new PropertyValueFactory<>("medium"));
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        minusCol.setCellValueFactory(new PropertyValueFactory<>("minus"));
        plusCol.setCellValueFactory(new PropertyValueFactory<>("plus"));
        xCol.setCellValueFactory(new PropertyValueFactory<>("x"));

        data = obsDTOs;

        cartView.setItems(data);

        cartView.getSelectionModel().clearSelection();

        // calculate and set total price
        if (rmiController.getCart().getCartLineItems().size() == 0) {
            totalPriceLabel.setText("0 " + CURRENCY);
        } else {
            double totalPrice = calculateTotalPrice(data.iterator());
            DecimalFormat df = new DecimalFormat("#.00");
            totalPriceLabel.setText(df.format(totalPrice) + " " + CURRENCY);
        }

        determineButtonStates();
    }

    private double calculateTotalPrice(Iterator<CartLineItem> iter) {
        double totalPrice = 0;

        while (iter.hasNext()) {
            CartLineItem cartLineItem = iter.next();
            double price = cartLineItem.getPrice().doubleValue();
            int quantity = cartLineItem.getQuantity();
            totalPrice += price * quantity;
        }

        return totalPrice;
    }

    @FXML
    protected void cartLineItemEdited(MouseEvent e) throws IOException, AlbumNotFoundException, NoPermissionException {

        if (e.isPrimaryButtonDown() && !cartView.getItems().isEmpty()) {

            CartLineItem cartLineItem = cartView.getSelectionModel().getSelectedItem();
            CartLineItemDTO cartLineItemDTO = cartLineItem.getLineItemDTO();

            // get selected row-index
            int selectedRowIdx = cartView.getSelectionModel().getSelectedIndex();

            // get selected column-index
            ObservableList<TablePosition> pos = cartView.getSelectionModel().getSelectedCells();

            int selectedColIdx = -1;

            for (TablePosition po : pos) {
                selectedColIdx = po.getColumn();
            }

            // product clicked
            if (selectedColIdx == PRODUCT_COLUMN_POSITION) {
                if (e.isPrimaryButtonDown() && e.getClickCount() == 2) {
                    AlbumDTO albumDTO = this.rmiController.findAlbumByAlbumTitleAndMedium(cartLineItem.getName(), cartLineItem.getMedium());
                    sceneSwitcher.switchSceneToProductOverviewView(e, albumDTO);
                }
            }

            // minus clicked
            else if (selectedColIdx == MINUS_COLUMN_POSITION) {
                if (cartLineItem.getQuantity() == 1) {
                    data.remove(selectedRowIdx);
                    rmiController.removeLineItemFromCart(cartLineItemDTO);
                } else {
                    data.set(selectedRowIdx, new CartLineItem(
                            cartLineItem.getName(),
                            cartLineItem.getMedium(),
                            cartLineItem.getStock(),
                            cartLineItem.getQuantity() - 1,
                            cartLineItem.getPrice(),
                            cartLineItem.getMinus(),
                            cartLineItem.getPlus(),
                            cartLineItem.getX(),
                            cartLineItem.getLineItemDTO()
                    ));

                    rmiController.changeQuantity(cartLineItemDTO, cartLineItem.getQuantity() - 1);
                }
            }

            // plus clicked
            else if (selectedColIdx == PLUS_COLUMN_POSITION) {

                if (cartLineItem.getQuantity() < cartLineItem.getStock()) {
                    data.set(selectedRowIdx, new CartLineItem(cartLineItem.getName(),
                            cartLineItem.getMedium(),
                            cartLineItem.getStock(),
                            cartLineItem.getQuantity() + 1,
                            cartLineItem.getPrice(),
                            cartLineItem.getMinus(),
                            cartLineItem.getPlus(),
                            cartLineItem.getX(),
                            cartLineItem.getLineItemDTO()
                    ));

                    rmiController.changeQuantity(cartLineItemDTO, cartLineItem.getQuantity() + 1);
                }
            }

            // x clicked
            else if (selectedColIdx == CROSS_COLUMN_POSITION) {
                data.remove(selectedRowIdx);
                rmiController.removeLineItemFromCart(cartLineItemDTO);
            }

            if (rmiController.getCart().getCartLineItems().size() == 0) {
                totalPriceLabel.setText("0 " + CURRENCY);
            } else {
                double totalPrice = calculateTotalPrice(data.iterator());
                DecimalFormat df = new DecimalFormat("#.00");
                totalPriceLabel.setText(df.format(totalPrice) + " " + CURRENCY);
            }

            // prevents table row selection highlighting
            cartView.getSelectionModel().clearSelection();
        }

        determineButtonStates();
    }

    @FXML
    protected void buyButtonClicked(ActionEvent event) {

        try {
            cartLineItemDTOs = rmiController.getCart().getCartLineItems();
            sceneSwitcher.switchSceneToCheckoutView(event, cartLineItemDTOs);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoPermissionException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void clearCartButtonClicked(ActionEvent actionEvent) {

        try {
            rmiController.clearCart();
            cartView.getItems().clear();

            determineButtonStates();

        } catch (NoPermissionException e) {
            e.printStackTrace();
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

    private void determineButtonStates() {
        boolean isCartEmpty = cartView.getItems().isEmpty();

        buyButton.setDisable(isCartEmpty);
        clearCartButton.setDisable(isCartEmpty);
    }
}