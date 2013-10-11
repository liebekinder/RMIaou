//package client;
//
//import java.net.BindException;
//import java.net.MalformedURLException;
//import java.rmi.AlreadyBoundException;
//import java.rmi.Naming;
//import java.rmi.NotBoundException;
//import java.rmi.RemoteException;
//import java.rmi.registry.LocateRegistry;
//
//import client.colorWrapper.ColorWrapperPseudo;
//
//import common.IChatRoom;
//import common.IChatRoomManager;
//
//public class Client {
//	
//	private String serverName;
//	private String chatRoomName;
//	private int serverPort;
//	private int clientPort;
//	private String serverIp;
//	private IChatRoomManager server;
//	private IChatRoom chatRoom;
//	private MessageListener clientListener;
//
//	public Client(int clientPort) throws BindException, RemoteException, MalformedURLException, AlreadyBoundException {
//		//Color wrapper can be changed.
//		clientListener = new MessageListener(new ColorWrapperPseudo(), chatRoomName, clientPort);
//		this.clientPort = clientPort;
//		
//		LocateRegistry.createRegistry(clientPort); 
//		Naming.bind("rmi://localhost:"+clientPort+"/client", clientListener);
//	}
//	
//	/**
//	 * Connect the client to a server.
//	 * 
//	 * @param serverName
//	 * @return
//	 * @throws NotBoundException 
//	 * @throws MalformedURLException 
//	 * @throws RemoteException
//	 */
//	public void connectToServer(String serverIp, String serverName, int port) throws MalformedURLException, RemoteException, NotBoundException {
//		this.serverIp = serverIp;
//		this.serverName = serverName;
//		this.serverPort = port;
//		
//        server = (IChatRoomManager) Naming.lookup("rmi://"+this.serverIp+":"+this.serverPort+"/"+this.serverName);
//	}
//	
//	/**
//	 * 
//	 * @return currently connected server
//	 */
//	public IChatRoomManager getServer() {
//		return server;
//	}
//	
//	/**
//	 * Set output.
//	 * @param gui
//	 */
//	public void setOutputGui(ClientChatRoomGUI gui) {
//	    clientListener.setOutputGui(gui);
//	}
//	
//	/**
//	 * 
//	 * @return currently connected chatRoom
//	 */
//	public IChatRoom getChatRoom() {
//		return chatRoom;
//	}
//	
//	/**
//	 * Connect the client to a server.
//	 * 
//	 * @param chatRoomName
//	 * @return
//	 * @throws NotBoundException 
//	 * @throws MalformedURLException 
//	 * @throws RemoteException
//	 */
//	public String connectToChatRoom(String chatRoomName) throws MalformedURLException, RemoteException, NotBoundException {
//		this.chatRoomName = chatRoomName;
//        chatRoom = (IChatRoom) Naming.lookup("rmi://"+this.serverIp+":"+this.serverPort+"/"+this.serverName+"/"+this.chatRoomName);
//        return chatRoom.register(clientPort, ClientConfig.pseudo);
//	}
//
//	public void send(String message) {
//		try {
//			chatRoom.send("["+ClientConfig.pseudo+"] "+message);
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
//	}
//
//    public void sendRaw(String message) {
//        try {
//            chatRoom.send(message);
//        } catch (RemoteException e) {
//            e.printStackTrace();
//        }
//    }
//
//}
