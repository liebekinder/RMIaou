package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import client.colorWrapper.IColorWrapper;
import common.IMessageListener;

public class MessageListener extends UnicastRemoteObject implements IMessageListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IColorWrapper colorWrapper;

	protected MessageListener(IColorWrapper colorWrapper) throws RemoteException {
		super();
		this.colorWrapper = colorWrapper;
	}


	public void sendMessage(String message) throws RemoteException {
		if(colorWrapper!=null)colorWrapper.addFormatedText(message+"\n");
	}


    public void setOutputGui(ClientChatRoomGUI gui) {
        colorWrapper.setOutputGui(gui); 
    }


    public void sendDeconnect() throws RemoteException {
        //TODO Déconnecter le client
        if(colorWrapper!=null)colorWrapper.addFormatedText("You've been deconnected !\n");
    }

}
