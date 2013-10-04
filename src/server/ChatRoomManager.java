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

	@Override
	public HashSet<String> getChatRoomsList() throws RemoteException {
		return server.getChatRoomsList();
	}

	@Override
	public String createChatRoom(String name) throws RemoteException {
		return server.createChatRoom(name);
	}

}