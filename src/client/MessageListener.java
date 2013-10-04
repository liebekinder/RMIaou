package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import common.IMessageListener;

public class MessageListener extends UnicastRemoteObject implements IMessageListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected MessageListener() throws RemoteException {
		super();
	}


	@Override
	public void sendMessage(String message) throws RemoteException {
		System.out.println(message);
	}

}
