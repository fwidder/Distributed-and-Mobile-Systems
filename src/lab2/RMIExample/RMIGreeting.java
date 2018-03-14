/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2.RMIExample;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIGreeting extends Remote{
    public String getGreeting() throws RemoteException;
    public void setGreeting(String message) throws RemoteException;
}
