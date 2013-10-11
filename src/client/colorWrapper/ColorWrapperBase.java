package client.colorWrapper;

import client.ChatRoomGui;

/**
 * Bas wrapper : do nothing.
 * @author Ornicare
 *
 */
public class ColorWrapperBase implements IColorWrapper{

	protected ChatRoomGui chatRoomGui;

	public ColorWrapperBase() {
	}

	public void addFormatedText(String text) {
		chatRoomGui.getClientChatRoomGui().addText(text);
	}

	public void setOutputGui(ChatRoomGui gui) {
		chatRoomGui = gui;
	}
}
