package client;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Enumeration;

import org.apache.log4j.BasicConfigurator;

public class LaunchClient {


	public static void main(String[] args) throws MalformedURLException,
			RemoteException, NotBoundException, AlreadyBoundException {

		Enumeration e;
		try {
			e = NetworkInterface.getNetworkInterfaces();
			while (e.hasMoreElements()) {
				NetworkInterface n = (NetworkInterface) e.nextElement();
				Enumeration ee = n.getInetAddresses();
				while (ee.hasMoreElements()) {
					InetAddress i = (InetAddress) ee.nextElement();
					System.out.println(i.getHostAddress());
				}
			}
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		BasicConfigurator.configure();
		// new ClientPrincipaleGUI();
		
		new ClientGui("RMIaou");
		// Client client = new Client(10011);
		// client.connectToServer("localhost", "MyFirstServer", 10010);
		// Set<String> chatRoomList = client.getServer().getChatRoomsList();
		// for(String chatRoomName : chatRoomList) {
		// System.out.println(chatRoomName);
		// }
		//
		// System.out.println(client.getServer().createChatRoom("camembert"));
		//
		// client.connectToChatRoom("camembert");
		//
		// client.send("Je suis un camembert");

	}
}
