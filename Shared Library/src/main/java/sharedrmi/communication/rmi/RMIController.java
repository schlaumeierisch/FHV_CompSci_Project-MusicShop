package sharedrmi.communication.rmi;

import sharedrmi.application.api.*;
import sharedrmi.domain.valueobjects.Role;

import javax.ejb.Remote;
import javax.security.auth.login.FailedLoginException;
import java.io.Serializable;
import java.nio.file.AccessDeniedException;
import java.rmi.RemoteException;
import java.util.List;

@Remote
public interface RMIController extends ProductService, ShoppingCartService, InvoiceService, CustomerService, MessageProducerService, UserService, Serializable {
    void login(String username, String password) throws FailedLoginException, RemoteException, AccessDeniedException;
    List<Role> getRoles();
    String getUsername();
}
