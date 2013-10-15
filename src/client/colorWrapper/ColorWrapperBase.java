package client.colorWrapper;

import client.gui.ChatRoomGui;

/**
 * Bas wrapper : do nothing.
 * @author Ornicare
 *
 */
public class ColorWrapperBase implements IColorWrapper{

	protected ChatRoomGui chatRoomGui;
	protected boolean inputs = true;

	public ColorWrapperBase() {
	}

	public void addFormatedText(String text) {
		if(inputs) chatRoomGui.getClientChatRoomGui().addText(text);
	}

	public void setOutputGui(ChatRoomGui gui) {
		chatRoomGui = gui;
	}

	@Override
	public void stopInputs() {
		chatRoomGui.stopInputs();
		inputs =false;
	}
}
