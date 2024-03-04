package at.fhv.musicshopfx;

import sharedrmi.application.dto.AlbumDTO;
import sharedrmi.communication.rmi.RMIController;

import javax.jms.JMSException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.security.auth.login.FailedLoginException;
import java.nio.file.AccessDeniedException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class SessionManager {

    private static SessionManager instance;
    private static String loggedInUsername;
    private static boolean isLoggedIn;
    private static String lastSearch = "";
    private static boolean newMessages = false;
    private static List<AlbumDTO> lastAlbums = new ArrayList<>();

    private final RMIController rmiController;


    private SessionManager(RMIController rmiController) {
        SessionManager.instance = this;
        this.rmiController = rmiController;
    }


    public static SessionManager getInstance() throws NotLoggedInException {
        if (!SessionManager.isLoggedIn) {
            throw new NotLoggedInException("Not logged in! Call SessionManager.login() first");
        }

        return SessionManager.instance;
    }

    public static boolean isNewMessageAvailable() {
        return newMessages;
    }

    public static void setNewMessages(boolean newMessages) {
        SessionManager.newMessages = newMessages;
    }

    public static boolean login(String username, String password, String server) throws FailedLoginException, AccessDeniedException {
        try {
            /*
            RMIControllerFactory rmiControllerFactory = (RMIControllerFactory) Naming.lookup("rmi://"+server+"/RMIControllerFactory");
            RMIController rmiController = rmiControllerFactory.createRMIController(username, password);
             */
            Properties props = new Properties();
            props.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
            props.put(Context.PROVIDER_URL, "http-remoting://"+server);
            props.put("jboss.naming.client.ejb.context", true);
            Context ctx = new InitialContext(props);

            //ejb:/[DeployedName]/Implementierungsname![packages + Interface of Bean]
            RMIController rmiController = (RMIController) ctx.lookup("ejb:/musicshop-1.0/RMIControllerImpl!sharedrmi.communication.rmi.RMIController");

            rmiController.login(username, password);

            new SessionManager(rmiController);
            SessionManager.loggedInUsername = username;
            SessionManager.isLoggedIn = true;

            return true;

        } catch (RemoteException|NamingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void logout() throws NotLoggedInException {
        if (SessionManager.isLoggedIn) {
            SessionManager.instance = null;
            SessionManager.loggedInUsername = "";

            SessionManager.lastSearch = "";
            SessionManager.lastAlbums = new ArrayList<>();
            try {
                MessageConsumerService messageConsumerService = MessageConsumerServiceImpl.getInstance();
                messageConsumerService.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
            SessionManager.isLoggedIn = false;
        } else {
            throw new NotLoggedInException("Not logged in! Call SessionManager.login() first");
        }
    }

    public RMIController getRMIController() {
        return rmiController;
    }

    public static String getLoggedInUsername() {
        return SessionManager.loggedInUsername;
    }

    public static String getLastSearch() {
        return SessionManager.lastSearch;
    }

    public static void setLastSearch(String lastSearch) {
        SessionManager.lastSearch = lastSearch;
    }

    public static List<AlbumDTO> getLastAlbums() {
        return SessionManager.lastAlbums;
    }

    public static void setLastAlbums(List<AlbumDTO> albums) {
        SessionManager.lastAlbums = albums;
    }

    // updates AlbumDTO in search after stock change due to return or buy
    public static void updateLastAlbums(AlbumDTO updatedAlbumDTO) {
        if (SessionManager.lastAlbums.size() > 0) {
            SessionManager.lastAlbums = SessionManager.lastAlbums
                    .stream()
                    .map(item -> {
                        if (item.getAlbumId().equals(updatedAlbumDTO.getAlbumId())) {
                            return updatedAlbumDTO;
                        }

                        return item;
                    })
                    .collect(Collectors.toList());
        }
    }
}