package client;

import java.awt.Color;
import java.rmi.RemoteException;

import client.gui.ChatRoomGui;
import common.IChatRoom;

public class ChatRoomWrapper {
    
    private IChatRoom chatRoom;
	private ChatRoomGui gui;

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
        	if(gui!=null) {
        		gui.addText("Server does not respond. Disconnected.\n", Color.RED);
        		deconnect();
        	}
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
        	gui.disableInput();
            chatRoom.deconnect(ClientConfig.pseudo);
        } catch (RemoteException e) {
            
        }
    }

	public void setOutputGui(ChatRoomGui chatRoom ) {
		gui = chatRoom;
	}

}
