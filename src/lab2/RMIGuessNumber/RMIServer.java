package lab2.RMIGuessNumber;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Florian Widder
 * @author Student ID 18999061
 *
 */
public class RMIServer implements RMIGuessNumber {

    public static void main(String[] args) {
	System.out.println("In the Main Method of RMIServer...");
	RMIServer remoteObject = new RMIServer((int) (Math.random() * 100));
	System.out.println("RMIServer Object is created...");
	try {
	    System.out.println("RMIServer: Creating the UnicasteRemoteObject...");
	    RMIGuessNumber stub = (RMIGuessNumber) UnicastRemoteObject.exportObject(remoteObject, 0);
	    System.out.println("RMIServer: Creating the Registry...");
	    Registry registry = LocateRegistry.getRegistry();
	    System.out.println("RMIServer: Registring the Remote object with name GuessNumberRMI...");
	    registry.rebind("GuessNumberRMI", stub);
	    System.out.println("Remote Object is bound with RMI Registry...");
	    try {
		String[] bindings = Naming.list("localhost");
		for (String name : bindings) {
		    System.out.println(name);
		}
	    } catch (MalformedURLException e) {
		System.err.println("Unable to see names: " + e);
	    }
	} catch (RemoteException e) {
	    System.err.println("Unable to bind to registry. " + e);
	}
    }

    private int number;

    private int guess;

    public RMIServer(int number) {
	this.number = number;
    }

    @Override
    public String getNumber() throws RemoteException {
	if (number == guess) {
	    return "Number " + guess + " is it!";
	} else if (number < guess) {
	    return "Number " + guess + " is to high!";
	} else {
	    return "Number " + guess + " is to low!";
	}
    }

    @Override
    public void restart() throws RemoteException {
	number = (int) (Math.random() * 100);
	System.out.println("Restart Game: New Number is: " + number);
    }

    @Override
    public void setGuess(int number) throws RemoteException {
	guess = number;
	System.out.println("New guess: " + number);
	if (this.number == guess) {
	    System.out.println("Number " + guess + " is it!");
	} else if (this.number < guess) {
	    System.out.println("Number " + guess + " is to high!");
	} else {
	    System.out.println("Number " + guess + " is to low!");
	}
    }
}
