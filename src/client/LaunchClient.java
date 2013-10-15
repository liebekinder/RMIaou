package client;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import org.apache.log4j.BasicConfigurator;

public class LaunchClient {


	public static void main(String[] args) throws MalformedURLException,
			RemoteException, NotBoundException, AlreadyBoundException {

//		Enumeration e;
//		try {
//			e = NetworkInterface.getNetworkInterfaces();
//			while (e.hasMoreElements()) {
//				NetworkInterface n = (NetworkInterface) e.nextElement();
//				Enumeration ee = n.getInetAddresses();
//				while (ee.hasMoreElements()) {
//					InetAddress i = (InetAddress) ee.nextElement();
//					System.out.println(i.getHostAddress());
//				}
//			}
//		} catch (SocketException e1) {
//			e1.printStackTrace();
//		}

		BasicConfigurator.configure();
		
		
		// new ClientPrincipaleGUI();		
		new ClientGui("RMIaou");

	}
}
