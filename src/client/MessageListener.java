package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import client.colorWrapper.IColorWrapper;
import client.gui.ChatRoomGui;
import common.IMessageListener;



public class MessageListener extends UnicastRemoteObject implements
        IMessageListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private IColorWrapper colorWrapper;

    private String chatRoomName;

    private Client client;

    protected MessageListener(IColorWrapper colorWrapper, String chatRoomName, Client client)
            throws RemoteException {
        super();
        this.colorWrapper = colorWrapper;
        this.chatRoomName = chatRoomName;
        this.client = client;
    }

    public void sendMessage(String message) throws RemoteException {
        if (colorWrapper != null && message != null)
            colorWrapper.addFormatedText(message + "\n");
    }

    public void setOutputGui(ChatRoomGui chatGui) {
        colorWrapper.setOutputGui(chatGui);
    }

    public synchronized void sendDeconnect() throws RemoteException {
        if(colorWrapper!=null) {
            client.deleteChatRoom(chatRoomName);
            colorWrapper.addFormatedText("You've been deconnected !\n");
            colorWrapper.stopInputs();
        }
    }

    public Object getPseudo() throws RemoteException {
        return ClientConfig.pseudo;
    }

	public void actualise() throws RemoteException {
		client.actualise();
	}
    

}
