package server;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

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

    public ChatRoom(String name, Server server, String owner)
            throws RemoteException, MalformedURLException,
            AlreadyBoundException {
        this.name = name;
        this.owner = owner;
        this.lock = new Lock();
        this.server = server;
        remoteClientsList = new ArrayList<IMessageListener>();

        Naming.bind(
                "rmi://localhost:" + server.getPort() + "/" + server.getName()
                        + "/" + this.name, this);
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
     * Monitor
     * @author Ornicare
     *
     */
    class Lock {}

    /**
     * Used by a client to subscribe to this chatroom
     */
    public synchronized String register(int port) throws RemoteException {
        if(stopRegistration) return "ERR : ChatRoom not available !";
        synchronized (lock) {
            IMessageListener clientListener = null;
            try {
                clientListener = (IMessageListener) Naming.lookup("rmi://"
                        + RemoteServer.getClientHost() + ":" + port + "/client");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (NotBoundException e) {
                e.printStackTrace();
            } catch (ServerNotActiveException e) {
                e.printStackTrace();
            }

            remoteClientsList.add(new ClientRemoteListener(clientListener));
        }
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
                listener.sendDeconnect();
            } catch (RemoteException e) {
                e.printStackTrace();
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

}
