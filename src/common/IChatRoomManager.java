package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.HashSet;

/**
 * Client interface for chatroom management
 * 
 * @author Ornicare
 *
 */
public interface IChatRoomManager extends Remote {
	/**
	 * Return the list of chatroom name's
	 * @return
	 * @throws RemoteException
	 */
	public HashSet<String> getChatRoomsList() throws RemoteException;
	
	/**
	 * Create a chatroom by using <code>name</code>
	 * 
	 * @param name The name of the chatroom that will be created
	 * @return A String that indicate if the process is successful or not
	 * @throws RemoteException
	 */
	public String createChatRoom(String name, String owner) throws RemoteException;
	
	/**
	 * Delete  a chatroom by using <code>name</code>
	 * 
	 * @param name The name of the chatroom to delete
	 * @return A String that indicate if the process is successful or not
	 * @throws RemoteException
	 */
	public String deleteChatRoom(String name, String pseudo) throws RemoteException;

	public String getClients(String chatRoomName) throws RemoteException;
}
