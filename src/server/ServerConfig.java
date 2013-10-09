package server;


public class ServerConfig {

	/**
	 * Each cleanDelay, we verify if client exists
	 */
	public static long cleanDelay = 3*60*1000;
	/**
	 * Auto destruct timer
	 */
	public static  int timeoutTime = 10*60*1000;

}
