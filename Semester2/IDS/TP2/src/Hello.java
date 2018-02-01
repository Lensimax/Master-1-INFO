import java.rmi.*;

public interface Hello extends Remote {
    public String sayHello()  throws RemoteException;
    public String sayName(String name) throws RemoteException;
}
