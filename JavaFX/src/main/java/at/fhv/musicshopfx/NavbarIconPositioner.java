package at.fhv.musicshopfx;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import sharedrmi.communication.rmi.RMIController;
import sharedrmi.domain.valueobjects.Role;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class NavbarIconPositioner {

    private final static SceneSwitcher sceneSwitcher = new SceneSwitcher();

    private final Image SEARCH_IMAGE = new Image(Launcher.class.getResourceAsStream("/images/search.png"));
    private final Image CART_IMAGE = new Image(Launcher.class.getResourceAsStream("/images/shopping-Cart.png"));
    private final Image INVOICE_IMAGE = new Image(Launcher.class.getResourceAsStream("/images/invoice.png"));
    private final Image PUBLISH_IMAGE = new Image(Launcher.class.getResourceAsStream("/images/writeMessage.png"));
    private final Image MESSAGE_IMAGE = new Image(Launcher.class.getResourceAsStream("/images/envelope.png"));
    private final Image NEW_MESSAGE_IMAGE = new Image(Launcher.class.getResourceAsStream("/images/envelopered.png"));
    private final Image SETTINGS_IMAGE = new Image(Launcher.class.getResourceAsStream("/images/settings.png"));

    private ImageView searchIcon;
    private ImageView cartIcon;
    private ImageView invoiceIcon;
    private ImageView publishIcon;
    private ImageView messageIcon;
    private ImageView newMessageIcon;
    private ImageView settingsIcon;

    private RMIController rmiController;
    private List<Role> roles;

    public NavbarIconPositioner() {

        try {
            this.rmiController = SessionManager.getInstance().getRMIController();
            this.roles = rmiController.getRoles();

            int imgHeight = 28;
            int imgWidth = 28;

            this.searchIcon = new ImageView(SEARCH_IMAGE);
            this.cartIcon = new ImageView(CART_IMAGE);
            this.invoiceIcon = new ImageView(INVOICE_IMAGE);
            this.publishIcon = new ImageView(PUBLISH_IMAGE);
            this.messageIcon = new ImageView(MESSAGE_IMAGE);
            this.newMessageIcon = new ImageView(NEW_MESSAGE_IMAGE);
            this.settingsIcon = new ImageView(SETTINGS_IMAGE);

            this.searchIcon.setPickOnBounds(true);
            this.cartIcon.setPickOnBounds(true);
            this.invoiceIcon.setPickOnBounds(true);
            this.publishIcon.setPickOnBounds(true);
            this.messageIcon.setPickOnBounds(true);
            this.newMessageIcon.setPickOnBounds(true);
            this.settingsIcon.setPickOnBounds(true);

            this.searchIcon.setCursor(Cursor.HAND);
            this.cartIcon.setCursor(Cursor.HAND);
            this.invoiceIcon.setCursor(Cursor.HAND);
            this.publishIcon.setCursor(Cursor.HAND);
            this.messageIcon.setCursor(Cursor.HAND);
            this.newMessageIcon.setCursor(Cursor.HAND);
            this.settingsIcon.setCursor(Cursor.HAND);

            this.searchIcon.setFitHeight(imgHeight);
            this.searchIcon.setFitWidth(imgWidth);
            this.cartIcon.setFitHeight(imgHeight);
            this.cartIcon.setFitWidth(imgWidth);
            this.invoiceIcon.setFitHeight(imgHeight);
            this.invoiceIcon.setFitWidth(imgWidth);
            this.publishIcon.setFitHeight(imgHeight);
            this.publishIcon.setFitWidth(imgWidth);
            this.messageIcon.setFitHeight(imgHeight);
            this.messageIcon.setFitWidth(imgWidth);
            this.newMessageIcon.setFitHeight(imgHeight);
            this.newMessageIcon.setFitWidth(imgWidth);
            this.settingsIcon.setFitHeight(imgHeight);
            this.settingsIcon.setFitWidth(imgWidth);

        } catch (NotLoggedInException e) {
            e.printStackTrace();
        }
    }

    public void positionIcons(VBox navbarVbox) throws FileNotFoundException {

        searchIcon.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.isPrimaryButtonDown()) {
                    try {
                        sceneSwitcher.switchSceneToMusicSearchView(mouseEvent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        VBox.setMargin(searchIcon, new Insets(15.0, 0.0, 15.0, 15.0));
        navbarVbox.getChildren().add(searchIcon);

        if(roles.contains(Role.SALESPERSON)) {
            cartIcon.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.isPrimaryButtonDown()) {
                        try {
                            sceneSwitcher.switchSceneToCartView(mouseEvent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            VBox.setMargin(cartIcon, new Insets(15.0, 0.0, 15.0, 15.0));
            navbarVbox.getChildren().add(cartIcon);


            invoiceIcon.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.isPrimaryButtonDown()) {
                        try {
                            sceneSwitcher.switchSceneToInvoiceSearchView(mouseEvent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            VBox.setMargin(invoiceIcon, new Insets(15.0, 0.0, 15.0, 17.0));
            navbarVbox.getChildren().add(invoiceIcon);
        }

        if(roles.contains(Role.OPERATOR)) {
            publishIcon.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.isPrimaryButtonDown()) {
                        try {
                            sceneSwitcher.switchSceneToMessageProducerView(mouseEvent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            VBox.setMargin(publishIcon, new Insets(15.0, 0.0, 15.0, 19.0));
            navbarVbox.getChildren().add(publishIcon);
        }
        if (SessionManager.isNewMessageAvailable()) {
            newMessageIcon.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.isPrimaryButtonDown()) {
                        try {
                            sceneSwitcher.switchSceneToMessageBoardView(mouseEvent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            VBox.setMargin(newMessageIcon, new Insets(15.0, 0.0, 15.0, 19.0));
            navbarVbox.getChildren().add(newMessageIcon);


        } else {
            messageIcon.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.isPrimaryButtonDown()) {
                        try {
                            sceneSwitcher.switchSceneToMessageBoardView(mouseEvent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            VBox.setMargin(messageIcon, new Insets(15.0, 0.0, 15.0, 19.0));
            navbarVbox.getChildren().add(messageIcon);
        }

        settingsIcon.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.isPrimaryButtonDown()) {
                    try {
                        sceneSwitcher.switchSceneToSettingsView(mouseEvent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        VBox.setMargin(settingsIcon, new Insets(15.0, 0.0, 15.0, 18.0));
        navbarVbox.getChildren().add(settingsIcon);
    }
}
