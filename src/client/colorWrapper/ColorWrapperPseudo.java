package client.colorWrapper;

import java.awt.Color;

/**
 * Change color for pseudo and system messages.
 * @author Ornicare
 *
 */
public class ColorWrapperPseudo extends ColorWrapperBase implements IColorWrapper{

	@Override
	public void addFormatedText(String text) {

		String[] pseudo = text.split("\\]");
		if(pseudo.length>1) {
			
			chatRoomGui.getClientChatRoomGui().addText("[",Color.RED);
			chatRoomGui.getClientChatRoomGui().addText(pseudo[0].substring(1), Color.LIGHT_GRAY);
			chatRoomGui.getClientChatRoomGui().addText("]",Color.RED);
			chatRoomGui.getClientChatRoomGui().addText(text.substring(pseudo[0].length()+1));
		}
		else {
			chatRoomGui.getClientChatRoomGui().addText(text,Color.ORANGE);
		}

	}
}
