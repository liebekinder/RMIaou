package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import common.IMessageListener;

public class MessageListener extends UnicastRemoteObject implements IMessageListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    private ClientChatRoomGUI gui;

	protected MessageListener() throws RemoteException {
		super();
	}


	public void sendMessage(String message) throws RemoteException {
		if(gui!=null)gui.addText(message+"\n");
	}


    public void setOutputGui(ClientChatRoomGUI gui) {
        this.gui = gui; 
    }

}
