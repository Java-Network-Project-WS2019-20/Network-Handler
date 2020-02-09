package networkHandler;


/**
 * This class implements an exception for missing arguments.
 * The user should provide at least one command line argument / program argument.
 * 
 * @author Sebastian Monok
 *
 */

public class NoArgumentException extends Exception {
	
	private static final long serialVersionUID = 1L;

	/**
	 * Empty Constructor
	 */
	public NoArgumentException () { }
	
	/**
	 * Default Constructor given a message as parameter.
	 * @param msg message for the exception, used to call super constructor.
	 */
	public NoArgumentException ( String msg ) {
		super ( msg );
	}
	
}
