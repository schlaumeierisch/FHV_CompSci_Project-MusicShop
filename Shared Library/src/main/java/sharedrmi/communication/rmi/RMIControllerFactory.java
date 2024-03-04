package sharedrmi.communication.rmi;

import javax.ejb.Remote;
import javax.security.auth.login.FailedLoginException;
import java.io.Serializable;
import java.nio.file.AccessDeniedException;
import java.rmi.RemoteException;

@Remote
public interface RMIControllerFactory extends Serializable {

    RMIController createRMIController(String username, String password) throws FailedLoginException, RemoteException, AccessDeniedException;

}