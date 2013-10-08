package client.colorWrapper;

import client.ClientChatRoomGUI;

public class ColorWrapperBase implements IColorWrapper{

	protected ClientChatRoomGUI chatRoomGui;

	public ColorWrapperBase() {
	}

	public void addFormatedText(String text) {
		chatRoomGui.addText(text);
	}

	@Override
	public void setOutputGui(ClientChatRoomGUI gui) {
		chatRoomGui = gui;
	}
}
