package at.fhv.musicshopfx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import sharedrmi.application.dto.AlbumDTO;
import sharedrmi.application.dto.ArtistDTO;
import sharedrmi.application.dto.MessageDTO;
import sharedrmi.application.dto.SongDTO;
import sharedrmi.application.exceptions.AlbumNotFoundException;
import sharedrmi.communication.rmi.RMIController;
import sharedrmi.domain.valueobjects.Role;

import javax.naming.NoPermissionException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@SuppressWarnings("rawtypes")
public class MusicOverviewController {
    @FXML
    private Label albumTitleLabel;
    @FXML
    private Label artistLabel;
    @FXML
    private Label mediumTypeLabel;
    @FXML
    private Label releaseDateLabel;
    @FXML
    private Label stockLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private TableView songsTableView;
    @FXML
    private TableColumn titleCol;
    @FXML
    private TableColumn genreCol;
    @FXML
    private TableColumn artistCol;
    @FXML
    private Button orderButton;
    @FXML
    private Button addToCartButton;
    @FXML
    private TextField quantityTextField;
    @FXML
    private Label invalidQtyErrorLabel;
    @FXML
    private Label addToCartErrorLabel;
    @FXML
    private Label orderSuccessLabel;
    @FXML
    private Label quantityLabel;
    @FXML
    private VBox navbarVbox;

    private RMIController rmiController;
    private AlbumDTO currentAlbumDTO;
    private List<Role> roles;
    private NavbarIconPositioner navbarIconPositioner = new NavbarIconPositioner();

    private final SceneSwitcher sceneSwitcher = new SceneSwitcher();

    @SuppressWarnings("unchecked")
    public void setData(AlbumDTO albumDTO) {

        try {
            this.rmiController = SessionManager.getInstance().getRMIController();
            this.roles = rmiController.getRoles();
            navbarIconPositioner.positionIcons(navbarVbox);

        } catch (NotLoggedInException |  FileNotFoundException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

        currentAlbumDTO = albumDTO;
        Set<SongDTO> songs = albumDTO.getSongs();
        albumTitleLabel.setText(albumDTO.getTitle());
        mediumTypeLabel.setText(albumDTO.getMediumType().toString());
        releaseDateLabel.setText(albumDTO.getReleaseDate().toString());
        stockLabel.setText(Integer.toString(albumDTO.getStock()));
        priceLabel.setText(albumDTO.getPrice().toString());

        Iterator<SongDTO> iter = albumDTO.getSongs().iterator();
        List<ArtistDTO> artists = iter.next().getArtists();
        ArtistDTO artist = artists.get(0);
        artistLabel.setText(artist.getName());

        ObservableList<SongDTO> songDTOs = FXCollections.observableArrayList(songs);

        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
        artistCol.setCellValueFactory(new PropertyValueFactory<>("artists"));

        songsTableView.setItems(songDTOs);

        for (Role role : this.roles) {
            if (role.equals(Role.SALESPERSON)) {
                this.quantityLabel.setVisible(true);
                this.quantityTextField.setVisible(true);
                this.orderButton.setVisible(true);
                this.addToCartButton.setVisible(true);
            }
        }
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
    protected void settingsSymbolClicked(MouseEvent e) throws IOException {
        if (e.isPrimaryButtonDown())
            sceneSwitcher.switchSceneToSettingsView(e);
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
    private void addToCartButtonClicked(ActionEvent event) throws IOException {
        try {
            int qty = Integer.parseInt(quantityTextField.getText());

            if (qty < 1) {
                this.showInvalidQtyErrorLabel();
            } else if (qty > Integer.parseInt(stockLabel.getText())) {
                this.showAddToCartErrorLabel();
            } else {
                rmiController.addAlbumsToCart(currentAlbumDTO, Integer.parseInt(quantityTextField.getText()));
                sceneSwitcher.switchSceneToMusicSearchView(event);
            }

        } catch (NumberFormatException e) {
            this.showInvalidQtyErrorLabel();
        } catch (NoPermissionException e) {
            e.printStackTrace();
        }

    }

    @FXML
    protected void orderButtonClicked(ActionEvent actionEvent) throws RemoteException, NoPermissionException {

        disableButtons(true);

        try {
            int qty = Integer.parseInt(quantityTextField.getText());

            if (qty < 1) {
                this.showInvalidQtyErrorLabel();
            } else {

                AlbumDTO albumDTO = rmiController.findAlbumByAlbumTitleAndMedium(currentAlbumDTO.getTitle(), currentAlbumDTO.getMediumType());

                MessageDTO message = MessageDTO.builder()
                        .messageText("Please order "+qty+" of "+albumDTO.getTitle()+" as "+albumDTO.getMediumType())
                        .messageTitle("Order Request")
                        .expirationDays(0).build();

                rmiController.publish(List.of("order"),message);

                rmiController.increaseStockOfAlbum(
                        albumDTO.getTitle(),
                        albumDTO.getMediumType(),
                        qty
                );

                int newQtyValue = albumDTO.getStock() + qty;
                stockLabel.setText(String.valueOf(newQtyValue));

                currentAlbumDTO = AlbumDTO.builder()
                        .albumId(albumDTO.getAlbumId())
                        .label(albumDTO.getLabel())
                        .mediumType(albumDTO.getMediumType())
                        .price(albumDTO.getPrice())
                        .releaseDate(albumDTO.getReleaseDate())
                        .songs(albumDTO.getSongs())
                        .stock(newQtyValue)
                        .title(albumDTO.getTitle())
                        .build();

                SessionManager.updateLastAlbums(currentAlbumDTO);

                this.showOrderSuccessLabel();
            }

        } catch (NumberFormatException e) {
            this.showInvalidQtyErrorLabel();

        } catch (AlbumNotFoundException e) {
            throw new RuntimeException(e);

        }

        disableButtons(false);

    }

    protected void showInvalidQtyErrorLabel() {
        orderSuccessLabel.setVisible(false);
        addToCartErrorLabel.setVisible(false);
        invalidQtyErrorLabel.setVisible(true);
    }

    protected void showAddToCartErrorLabel() {
        orderSuccessLabel.setVisible(false);
        invalidQtyErrorLabel.setVisible(false);
        addToCartErrorLabel.setVisible(true);
    }

    protected void showOrderSuccessLabel() {
        invalidQtyErrorLabel.setVisible(false);
        addToCartErrorLabel.setVisible(false);
        orderSuccessLabel.setVisible(true);
    }

    protected void disableButtons(boolean disabled) {
        orderButton.setDisable(disabled);
        addToCartButton.setDisable(disabled);
    }

}
