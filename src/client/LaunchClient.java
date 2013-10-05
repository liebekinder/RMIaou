package client;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Set;


public class LaunchClient {
	
	public static void main(String[] args) throws MalformedURLException, RemoteException, NotBoundException, AlreadyBoundException {
	    
	    new ClientPrincipaleGUI();
//		Client client = new Client(10011);
//		client.connectToServer("localhost", "MyFirstServer", 10010);
//		Set<String> chatRoomList = client.getServer().getChatRoomsList();
//		for(String chatRoomName : chatRoomList) {
//			System.out.println(chatRoomName);
//		}
//		
//		System.out.println(client.getServer().createChatRoom("camembert"));
//		
//		client.connectToChatRoom("camembert");
//		
//		client.send("Je suis un camembert");
		

		
	}
}
