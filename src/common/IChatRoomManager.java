package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.Set;

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
	public Set<String> getChatRoomsList() throws RemoteException;
	
	/**
	 * Create a chatroom by using <code>name</code>
	 * 
	 * @param name The name of the chatroom that will be created
	 * @return A String that indicate if the process is successful or not
	 * @throws RemoteException
	 */
	public String createChatRoom(String name) throws RemoteException;
	
	/**
	 * Delete  a chatroom by using <code>name</code>
	 * 
	 * @param name The name of the chatroom to delete
	 * @return A String that indicate if the process is successful or not
	 * @throws RemoteException
	 */
	//TODO extension
//	public String deleteChatRoom(String name) throws RemoteException;
}
