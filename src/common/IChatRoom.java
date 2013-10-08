package common;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * A magnificent chatroom
 * 
 * @author Ornicare
 *
 */
public interface IChatRoom extends Remote{
	public void send(String message) throws RemoteException;
	public String register(int port) throws RemoteException;
	public String getOwner() throws RemoteException;
    public String delete() throws RemoteException;
}
