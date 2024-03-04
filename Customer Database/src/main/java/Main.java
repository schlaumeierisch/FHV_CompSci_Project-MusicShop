import application.CustomerServiceImpl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public static void main(String[] args) throws RemoteException, MalformedURLException {

        System.setProperty("java.rmi.server.hostname","10.0.40.163");
        LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

        Naming.rebind("rmi://localhost/CustomerService", new CustomerServiceImpl());

    }
}