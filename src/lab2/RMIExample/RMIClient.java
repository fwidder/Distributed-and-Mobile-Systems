/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2.RMIExample;

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
    
    public static void main(String [] args){
        try{
            Registry registry = LocateRegistry.getRegistry();
            System.out.println("Registry is created...");
            RMIGreeting remoteProxy = (RMIGreeting)registry.lookup("HelloRMI");
            System.out.println("RMI Greeting is " + remoteProxy.getGreeting());
            Scanner input = new Scanner(System.in);
            System.out.println("Enter a new message to set in the Remote Server: ");
            String message = input.nextLine();
            remoteProxy.setGreeting(message);
            System.out.println("RMI Greeting is " + remoteProxy.getGreeting());
        }catch (RemoteException e){
            System.err.println("Unable to use registry: " + e);
        }catch (NotBoundException e){
            System.err.println("Name greeting not currently bound: " + e);
        }catch(ClassCastException e){
            System.err.println("Class Cast Exception: " + e);
        }
    }
}
