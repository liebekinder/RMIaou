package server;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import common.IChatRoom;
import common.IChatRoomManager;

public class Server implements IChatRoomManager{

	private Map<String, IChatRoom> chatRoomList;
	private String serverName;
	private IChatRoomManager chatRoomManager;
	private int port;
	
	public Server(String serverName, int port) throws RemoteException, MalformedURLException, AlreadyBoundException {
		this.serverName = serverName;
		chatRoomList = new HashMap<String, IChatRoom>();
		this.chatRoomManager = new ChatRoomManager(this);
		this.port = port;

		//System.setProperty("java.rmi.server.hostname","192.168.0.17");
		LocateRegistry.createRegistry(port); // réserve le port 8090
		Naming.bind("rmi://localhost:" + this.port + "/"+this.serverName, chatRoomManager);
			
	}


	/**
	 * Get the list of chatRoom name's
	 */
	@Override
	public Set<String> getChatRoomsList() throws RemoteException {
		return chatRoomList.keySet();
	}

	/**
	 * Try to create a new chatRoom. Return a success or an error message.
	 */
	@Override
	public String createChatRoom(String name) throws RemoteException {
		if(chatRoomList.containsKey(name)) return "E00: ChatRoom already exists";
		chatRoomList.put(name, new ChatRoom(name));
		return "S00: ChatRoom successfully created";
	}

}
