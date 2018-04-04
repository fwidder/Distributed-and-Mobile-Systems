package lab2.RMIExample;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Florian Widder
 * @author Student ID 18999061
 *
 */
public interface RMIGreeting extends Remote {
    public String getGreeting() throws RemoteException;

    public void setGreeting(String message) throws RemoteException;
}
