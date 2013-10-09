package client;

import java.net.BindException;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;

import client.colorWrapper.ColorWrapperPseudo;

import common.IChatRoom;
import common.IChatRoomManager;

public class Client2 {

	private String serverName;
	private String chatRoomName;
	private int serverPort;
	private int clientPort;
	private String serverIp;
	private IChatRoomManager server;
	private IChatRoom chatRoom;
	private List<MessageListener> clientListener;

	public Client2(int clientPort) throws BindException, RemoteException,
			MalformedURLException, AlreadyBoundException {
		// Color wrapper can be changed.
		clientListener = new ArrayList<MessageListener>();
		this.clientPort = clientPort;
		if (LocateRegistry.getRegistry(clientPort) == null)
			LocateRegistry.createRegistry(clientPort);
	}

	public MessageListener addMessageListener(String chatRoomName)
			throws RemoteException, MalformedURLException,
			AlreadyBoundException {

		MessageListener ml = new MessageListener(new ColorWrapperPseudo());
		clientListener.add(ml);
		Naming.bind("rmi://localhost:" + clientPort + "/" + ClientConfig.pseudo
				+ "-" + chatRoomName, ml);
		return ml;
	}

	/**
	 * Connect the client to a server.
	 * 
	 * @param serverName
	 * @return
	 * @throws NotBoundException
	 * @throws MalformedURLException
	 * @throws RemoteException
	 */
	public void connectToServer(String serverIp, String serverName, int port)
			throws MalformedURLException, RemoteException, NotBoundException {
		this.serverIp = serverIp;
		this.serverName = serverName;
		this.serverPort = port;

		server = (IChatRoomManager) Naming.lookup("rmi://" + this.serverIp
				+ ":" + this.serverPort + "/" + this.serverName);
	}

	/**
	 * 
	 * @return currently connected server
	 */
	public IChatRoomManager getServer() {
		return server;
	}

	/**
	 * 
	 * @return currently connected chatRoom
	 */
	public IChatRoom getChatRoom() {
		return chatRoom;
	}

	/**
	 * Connect the client to a server.
	 * 
	 * @param chatRoomName
	 * @return
	 * @throws NotBoundException
	 * @throws MalformedURLException
	 * @throws RemoteException
	 */
	public void connectToChatRoom(String chatRoomName)
			throws MalformedURLException, RemoteException, NotBoundException {
		this.chatRoomName = chatRoomName;
		chatRoom = (IChatRoom) Naming.lookup("rmi://" + this.serverIp + ":"
				+ this.serverPort + "/" + this.serverName + "/"
				+ this.chatRoomName);
		chatRoom.register(clientPort, ClientConfig.pseudo);
	}

	public void send(String message) {
		try {
			chatRoom.send("[" + ClientConfig.pseudo + "] " + message);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void sendRaw(String message) {
		try {
			chatRoom.send(message);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

//	public void unbind() throws RemoteException, MalformedURLException,
//			NotBoundException {
//		Naming.unbind("rmi://localhost:" + clientPort + "/client");
//
//	}

}
