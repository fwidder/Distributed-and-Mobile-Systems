/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2.RMIExample;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RMIServer implements RMIGreeting{
    
    private String message;
    
    public RMIServer(String message){
        this.message = message;
    }

    @Override
    public String getGreeting() throws RemoteException {
        System.out.println("getGreeting Method in Server is called.");
        return this.message;
    }

    @Override
    public void setGreeting(String message) throws RemoteException {
        System.out.println("setGreeting Method in Server is called.");
        this.message = message;
    }
    
    public static void main(String [] args){
        System.out.println("In the Main Method of RMIServer...");
        RMIServer remoteObject = new RMIServer("Hello Remote Server");
        System.out.println("RMIServer Object is created...");
        try{
            System.out.println("RMIServer: Creating the UnicasteRemoteObject...");
            RMIGreeting stub = (RMIGreeting) UnicastRemoteObject.exportObject(remoteObject, 0);
            System.out.println("RMIServer: Creating the Registry...");
            Registry registry = LocateRegistry.getRegistry();
            System.out.println("RMIServer: Registring the Remote object with name HelloRMI...");
            registry.rebind("HelloRMI", stub);
            System.out.println("Remote Object is bound with RMI Registry...");
            try{
                String []bindings = Naming.list("localhost");
                for(String name : bindings){
                    System.out.println(name);
                }
            }catch(MalformedURLException e){
                System.err.println("Unable to see names: " + e);
            }
        }catch(RemoteException e){
            System.err.println("Unable to bind to registry. " + e);
        }
    }   
}
