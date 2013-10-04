package common;

import java.rmi.Remote;

/**
 * A magnificent chatroom
 * 
 * @author Ornicare
 *
 */
public interface IChatRoom extends Remote{
	public void send(String message);
	public void register(IMessageListener clientListener);
}
