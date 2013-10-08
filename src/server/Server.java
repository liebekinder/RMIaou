package server;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import common.IChatRoom;
import common.IChatRoomManager;

/**
 * A chat server.
 * 
 * @author Ornicare
 *
 */
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
		LocateRegistry.createRegistry(port); 
		Naming.bind("rmi://localhost:" + this.port + "/"+this.serverName, chatRoomManager);
			
	}


	/**
	 * Get the list of chatRoom name's
	 */
	public HashSet<String> getChatRoomsList() throws RemoteException {
		return new HashSet<String>(chatRoomList.keySet());
	}

	/**
	 * Try to create a new chatRoom. Return a success or an error message.
	 */
	public String createChatRoom(String name, String owner) throws RemoteException {
		if(chatRoomList.containsKey(name)) return "E00: ChatRoom already exists";
		
		try {
			chatRoomList.put(name, new ChatRoom(name, this, owner));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}
		
		return "S00: ChatRoom successfully created";
	}


	public int getPort() {
		return port;
	}


	public String getName() {
		return serverName;
	}


    public String deleteChatRoom(String name, String pseudo)
            throws RemoteException {
        if(!chatRoomList.containsKey(name)) 
            return "ERR: Chatroom doesn't exists.";
        
        IChatRoom chatRoomToDelete = chatRoomList.get(name);
        
        if(!chatRoomToDelete.getOwner().equals(pseudo))
            return "ERR: You're not the owner of the chatroom";
        
        return chatRoomToDelete.delete();
    }


    public void formallyDeleteChatRoom(String name) {
        chatRoomList.remove(name);
    }

}
