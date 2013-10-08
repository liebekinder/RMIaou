package client.colorWrapper;

import client.ClientChatRoomGUI;

/**
 * Bas wrapper : do nothing.
 * @author Ornicare
 *
 */
public class ColorWrapperBase implements IColorWrapper{

	protected ClientChatRoomGUI chatRoomGui;

	public ColorWrapperBase() {
	}

	public void addFormatedText(String text) {
		chatRoomGui.addText(text);
	}

	public void setOutputGui(ClientChatRoomGUI gui) {
		chatRoomGui = gui;
	}
}
