package server;

import java.rmi.RemoteException;

import common.IMessageListener;

/**
 * Used to do not block all messages sending when a client do not respond.
 * 
 * @author Ornicare
 *
 */
public class ClientRemoteListener implements IMessageListener {

    /**
     * Save the pseudo used by the client to do not use the network to retrieve it
     */
    private String pseudo;
    
    public String getPseudo() throws RemoteException {
        return pseudo;
    }
    
	/**
	 * Handled client Listener
	 */
	private IMessageListener clientListener;
	/**
	 * Delete the client at the next step
	 */
	private boolean isRemovable;

	public ClientRemoteListener(IMessageListener clientListener, String pseudo) {
	    this.pseudo = pseudo;
		this.clientListener = clientListener;
	}

	/**
	 * Send the message to the client
	 */
	public synchronized void sendMessage(String message) throws RemoteException {
		clientListener.sendMessage(message);
	}

	public void setToRemove() {
		this.isRemovable = true;
	}
	
	public boolean isRemovable() {
		return isRemovable;
	}

    public void sendDeconnect() throws RemoteException {
        clientListener.sendDeconnect();
    }

}
