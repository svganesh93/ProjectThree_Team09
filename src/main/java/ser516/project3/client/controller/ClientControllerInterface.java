package ser516.project3.client.controller;

/**
 * @author Varun Srivastava, Adhiraj Tikku
 */
public interface ClientControllerInterface {
	
	/**
	 * Method to handle the start event of client application
	 * 
	 */
	void startClient();
	
	/**
	 * Method to connect to a server end point
	 * @param ipAddress
	 * @param port
	 */
	void toggleConnectionToServer(final String ipAddress, final int port);
	
	/**
	 * Forces the client to stop
	 */
	void stopClientConnector();

	/**
	 * Creates the Client View
	 */
	void initializeClientView();
}
