package client.colorWrapper;

import client.gui.ChatRoomGui;

/**
 * Color wrapper for chatroom gui
 * 
 * @author Ornicare
 *
 */
public interface IColorWrapper {
	/**
	 * Format the text give in entry and show it in a chat room gui
	 * @param text
	 */
	public void addFormatedText(String text);

	/**
	 * Set the output gui
	 * @param chatGui
	 */
	public void setOutputGui(ChatRoomGui chatGui);

	public void stopInputs();
}
