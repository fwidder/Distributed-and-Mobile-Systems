/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab2.RMIGuessNumber;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIGuessNumber extends Remote{
    public String getNumber() throws RemoteException;
    public void setGuess(int number) throws RemoteException;
    public void restart() throws RemoteException;
}
