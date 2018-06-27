package virtualchargepoint_api;

/**
 * Class for a custom exception, which is thrown if the user trys to stop a transaction, which is not running.
 */
@SuppressWarnings("serial")
public class NoTransactionException extends Exception{

	/**
	 * Default constructor for NoTransactionException
	 */
	public NoTransactionException() {}
	
	/**
	 * Parameterized constructor for NoTransactionException
	 * 
	 * @param message - The message which shall be printed if the exception occurs
	 */
	public NoTransactionException(String message) {
		super(message);
	}
}
