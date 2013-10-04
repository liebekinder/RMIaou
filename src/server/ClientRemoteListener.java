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
	 * Handled client Listener
	 */
	private IMessageListener clientListener;
	/**
	 * Delete the client at the next step
	 */
	private boolean isRemovable;

	public ClientRemoteListener(IMessageListener clientListener) {
		this.clientListener = clientListener;
	}

	/**
	 * Send the message to the client
	 */
	@Override
	public synchronized void sendMessage(String message) throws RemoteException {
		clientListener.sendMessage(message);
	}

	public void setToRemove() {
		this.isRemovable = true;
	}
	
	public boolean isRemovable() {
		return isRemovable;
	}

}
