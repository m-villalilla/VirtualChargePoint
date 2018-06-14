package ocpp_client_backend;

@SuppressWarnings("serial")
public class NoTransactionException extends Exception{

	public NoTransactionException() {}
	
	public NoTransactionException(String message) {
		super(message);
	}
}
