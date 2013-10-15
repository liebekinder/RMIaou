package server;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import common.IChatRoom;
import common.IMessageListener;

public class ChatRoom extends UnicastRemoteObject implements IChatRoom {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * List of clients who subscribes to this chatroom
     */
    private List<IMessageListener> remoteClientsList;

    private String name;

    private String owner;

    private Lock lock;

    private boolean stopRegistration;

    private Server server;

    /**
     * Timer used for purge chatroom when empty
     */
	private Timer timerChatroom;
	
	/**
	 * Auto-cleaner timer
	 */
	private Timer timerCleaner;
	

    public ChatRoom(String name, Server server, String owner)
            throws RemoteException, MalformedURLException,
            AlreadyBoundException {
        this.name = name;
        this.owner = owner;
        this.lock = new Lock();
        this.server = server;
        remoteClientsList = new ArrayList<IMessageListener>();
        timerCleaner = new Timer();
        timerChatroom = new Timer();

        Naming.bind(
                "rmi://localhost:" + server.getPort() + "/" + server.getName()
                        + "/" + this.name, this);
        
        timerCleaner.schedule(new TimerTask() {
			
			@Override
			public void run() {
				new Thread(new Runnable() {
					
					public void run() {
						/*
						 * IsALive packet...
						 */
						diffuse(null);
					}
				}).start();
				new Thread(new CleanRunnable()).start();
			}
		}, 0, ServerConfig.cleanDelay);
    }

    /**
     * Send a message to all the clients.
     */
    public void send(String message) throws RemoteException {
        diffuse(message);
    }

    /**
     * Return the name of the chatRoom
     * 
     * @return
     */
    public String getName() {
        return name;
    }
    
    /**
     * Chatroom timeout timer
     */
    public void launchTimer() {
    	timerChatroom.cancel();
    	timerChatroom.purge();
    	timerChatroom = new Timer();
    	timerChatroom.schedule(new TimerTask() {
    		  @Override
    		  public void run() {
    		    try {
					delete();
				} catch (RemoteException e) {
					System.out.println("Auto-deletion for "+name+" failed !");
				}
    		  }
    		}, ServerConfig.timeoutTime);
    }
    
    /**
     * Monitor
     * @author Ornicare
     *
     */
    class Lock {}

    /**
     * Used by a client to subscribe to this chatroom
     */
    public synchronized String register(int port, String pseudo) throws RemoteException {
        if(stopRegistration) return "ERR : ChatRoom not available !";
        synchronized (lock) {
            
            for(IMessageListener listener : remoteClientsList)
                if(pseudo.equals((listener.getPseudo()))) return "ERR : Pseudo already in use in this chatroom !";
            
            IMessageListener clientListener = null;
            try {
                System.out.println("rmi://"
                        + RemoteServer.getClientHost() + ":" + port + "/"+pseudo+"/"+this.name);
                clientListener = (IMessageListener) Naming.lookup("rmi://"
                        + RemoteServer.getClientHost() + ":" + port + "/"+pseudo+"/"+this.name);
            } catch (Exception e) {
                e.printStackTrace();
               	System.out.println("Failed to bind client !");
               	return "Failed to register client !";
            } 

            remoteClientsList.add(new ClientRemoteListener(clientListener, pseudo));
        }
        
        //Stop deletion timer
        timerChatroom.cancel();
        timerChatroom.purge();
        
        return "Register successfull !";
    }

    /**
     * Delete the chatroom
     */
    public synchronized String delete() throws RemoteException {
        //synchronized permit to not add a new client when deleting a chatroom. And vis versa
        synchronized (lock) {
            stopRegistration = true;
            DeleteThread deleteThread = new DeleteThread();
            for(IMessageListener listener : remoteClientsList) {
                // Add a new message to send to the corresponding client
                Thread clientDelete = new Thread(new DeleteClient(listener));
                deleteThread.waitFor(clientDelete);
                clientDelete.start();

            }

            new Thread(deleteThread).start();
            try {
				Naming.unbind(
				        "rmi://localhost:" + server.getPort() + "/" + server.getName()
				                + "/" + this.name);
			} catch (Exception e) {
				return "Cannot unbind chatroom but successfully deleted it";
			} 
        }
        return "Chatroom "+name+" successfully deleted !";
    }
    
    /**
     * Send deconnect message to client
     * @author Ornicare
     *
     */
    private class DeleteClient implements Runnable {

        private IMessageListener listener;

        public DeleteClient(IMessageListener listener) {
            this.listener = listener;
        }
        
        public void run() {
            try {
                diffuse(listener.getPseudo()+" have been deconnected.");
                listener.sendDeconnect();
            } catch (RemoteException e) {
                try {
					System.out.println("Cannot deconnect "+listener.getPseudo()+". Is it already deconnected ?");
				} catch (RemoteException e1) {
				}
            }
        }
        
    }
    
    /**
     * Delete thread.
     * 
     * @author Ornicare
     * 
     */
    private class DeleteThread implements Runnable {

        public void run() {
            server.formallyDeleteChatRoom(name);
        }

        /**
         * Wait for the end of <code>thread</code>
         * 
         * @param thread
         */
        public void waitFor(Thread thread) {
            try {
                thread.join(0);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * Diffuse the message to all the clients. Synchronized to avoid conflicts.
     * Create a new Thread to not bug the client.
     */
    private synchronized void diffuse(String message) {

        // Use to clean ghost client after all messages sending
        CleanRunnable cleanThread = new CleanRunnable();

        for (IMessageListener remoteMessageListener : remoteClientsList) {
            // Add a new message to send to the corresponding client
            Thread clientThread = new Thread(new ClientSender(
                    remoteMessageListener, message));
            cleanThread.waitFor(clientThread);
            clientThread.start();

        }

        // TODO (Test) In theory this thread start after the end of all others
        new Thread(cleanThread).start();

    }

    public String getOwner() throws RemoteException {
        return owner;
    }

    /**
     * Clean all removable client
     */
    private synchronized void clean() {
        List<IMessageListener> copyMessagesListeners = new ArrayList<IMessageListener>(
                remoteClientsList);

        for (IMessageListener remoteMessageListener : copyMessagesListeners) {
            if (((ClientRemoteListener) remoteMessageListener).isRemovable())
                remoteClientsList.remove(remoteMessageListener);
        }
        if(remoteClientsList.isEmpty()) launchTimer();
    }

    /**
     * Cleaner thread.
     * 
     * @author Ornicare
     * 
     */
    private class CleanRunnable implements Runnable {

        public void run() {
            clean();
        }

        /**
         * Wait for the end of <code>thread</code>
         * 
         * @param thread
         */
        public void waitFor(Thread thread) {
            try {
                thread.join(0);
            } catch (InterruptedException e) {
            }
        }
    }

    /**
     * Used to send message to the client. A new thread is created to stack
     * sending message in {@link ClientRemoteListener} synchronized method.
     * 
     * @author Ornicare
     * 
     */
    private class ClientSender implements Runnable {

        private IMessageListener remoteMessageListener;

        private String message;

        public ClientSender(IMessageListener remoteMessageListener,
                String message) {
            this.remoteMessageListener = remoteMessageListener;
            this.message = message;
        }

        public void run() {
            try {
                remoteMessageListener.sendMessage(message);
            } catch (RemoteException e) {
                // In case of error remove the client of the list ?
                ((ClientRemoteListener) remoteMessageListener).setToRemove();
            }
        }

    }

	public String deconnect(String pseudo) throws RemoteException {
		synchronized (lock) {
			IMessageListener toRemove = null;
			for(IMessageListener listener : remoteClientsList) {
				if(listener.getPseudo().equals(pseudo)) {
				    diffuse(listener.getPseudo()+" have been deconnected.");
					listener.sendDeconnect();
					toRemove = listener;
					break;
				}
			}
			remoteClientsList.remove(toRemove);
			if(remoteClientsList.isEmpty()) launchTimer();
		}
		return "Sucessfully deconnected !";
	}

	public int getConnectedClients() throws RemoteException {
		return remoteClientsList.size();
	}

}
