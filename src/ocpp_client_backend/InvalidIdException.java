package ocpp_client_backend;

@SuppressWarnings("serial")
public class InvalidIdException extends Exception{

	public InvalidIdException() {}
	
	public InvalidIdException(String message) {
		super(message);
	}
}
