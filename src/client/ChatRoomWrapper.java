package client;

import java.rmi.RemoteException;

import common.IChatRoom;

public class ChatRoomWrapper {
    
    private IChatRoom chatRoom;

    public ChatRoomWrapper(IChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
    
    public IChatRoom getChatRoom() {
        return chatRoom;
    }
    
    public void send(String message) {
        try {
            chatRoom.send("[" + ClientConfig.pseudo + "] " + message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sendRaw(String message) {
        try {
            chatRoom.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void deconnect() {
        try {
            chatRoom.deconnect(ClientConfig.pseudo);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
