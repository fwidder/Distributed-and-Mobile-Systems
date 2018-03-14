package lab2.RMIGuessNumber;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Florian Widder
 * @author Student ID 18999061
 *
 */
public interface RMIGuessNumber extends Remote {
	public String getNumber() throws RemoteException;

	public void setGuess(int number) throws RemoteException;

	public void restart() throws RemoteException;
}
