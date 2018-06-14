package ocpp_client_backend;

@SuppressWarnings("serial")
public class NotConnectedException extends Exception{

	public NotConnectedException() {}
	
	public NotConnectedException(String message) {
		super(message);
	}
}
