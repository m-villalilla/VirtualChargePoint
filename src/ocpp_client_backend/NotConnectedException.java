package ocpp_client_backend;

/**
 * Class for a custom exception, which is thrown if the chargepoint is not connected.
 */
@SuppressWarnings("serial")
public class NotConnectedException extends Exception{

	/**
	 * Default constructor for NotConnectedException
	 */
	public NotConnectedException() {}
	
	/**
	 * Parameterized constructor for NotConnectedException
	 * 
	 * @param message - The message which shall be printed if the exception occurs
	 */
	public NotConnectedException(String message) {
		super(message);
	}
}
