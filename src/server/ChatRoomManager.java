package server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashSet;

import common.IChatRoomManager;

public class ChatRoomManager extends UnicastRemoteObject implements IChatRoomManager {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3839388483214361332L;
	
	/**
	 * Owner of all the chatRooms
	 */
	private Server server;

	protected ChatRoomManager(Server server) throws RemoteException {
		super();
		this.server = server;
	}

	public HashSet<String> getChatRoomsList() throws RemoteException {
		return server.getChatRoomsList();
	}

	public String createChatRoom(String name, String owner) throws RemoteException {
		return server.createChatRoom(name, owner);
	}

    public String deleteChatRoom(String name, String pseudo)
            throws RemoteException {
        return server.deleteChatRoom(name, pseudo);
    }

	public String getClients(String chatRoomName) throws RemoteException {
		return server.getClients(chatRoomName);
	}

}
