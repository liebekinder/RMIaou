package server;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

public class LaunchServer {

	public static void main(String[] args) {
		try {
			//System.setProperty("java.rmi.server.hostname","localhost");
			new Server("MyFirstServer", 10010);
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}
	}

}
