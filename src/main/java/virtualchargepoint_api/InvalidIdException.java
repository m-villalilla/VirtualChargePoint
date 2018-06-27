package virtualchargepoint_api;

/**
 * Class for a custom exception, which is thrown if the authorization id is invalid.
 */
@SuppressWarnings("serial")
public class InvalidIdException extends Exception{

	/**
	 * Default constructor for InvalidIdException
	 */
	public InvalidIdException() {}
	
	/**
	 * Parameterized constructor for InvalidIdException
	 * 
	 * @param message - The message which shall be printed if the exception occurs
	 */
	public InvalidIdException(String message) {
		super(message);
	}
}
