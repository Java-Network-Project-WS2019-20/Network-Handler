package networkHandler;

/**
 * This class implements an exception for a missing argument.
 * The user should provide at least one command line argument / program argument.
 * 
 * @author Sebastian Monok
 *
 */

public class NoArgumentException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoArgumentException () { }
	
	public NoArgumentException ( String msg ) {
		super ( msg );
	}
	
}
