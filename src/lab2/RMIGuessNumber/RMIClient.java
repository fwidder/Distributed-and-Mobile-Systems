/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2.RMIGuessNumber;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/**
 *
 * @author mgamalik
 */
public class RMIClient {

	public static void main(String[] args) {
		try {
			Registry registry = LocateRegistry.getRegistry();
			System.out.println("Registry is created...");
			RMIGuessNumber remoteProxy = (RMIGuessNumber) registry.lookup("GuessNumberRMI");
			Scanner input = new Scanner(System.in);
			System.out.println("Enter a new number to set in the Remote Server: ");
			String message = input.nextLine();
			remoteProxy.setGuess(Integer.parseInt(message));
			System.out.println(remoteProxy.getNumber());
			boolean right = remoteProxy.getNumber().endsWith(" is it!");
			while (!right) {
				System.out.println("Enter a new number to set in the Remote Server: ");
				message = input.nextLine();
				remoteProxy.setGuess(Integer.parseInt(message));
				System.out.println(remoteProxy.getNumber());
				right = remoteProxy.getNumber().endsWith(" is it!");
			}
			remoteProxy.restart();
		} catch (RemoteException e) {
			System.err.println("Unable to use registry: " + e);
		} catch (NotBoundException e) {
			System.err.println("Name greeting not currently bound: " + e);
		} catch (ClassCastException e) {
			System.err.println("Class Cast Exception: " + e);
		}
	}
}
