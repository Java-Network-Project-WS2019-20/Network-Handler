package networkHandler;

/**
 * <p>Interface for calculations of properties of a {@link Graph}. It defines methods necessary for uniform calculation
 * and returning of results of different properties. 
 * <p>The extension of the {@link Runnable} interface enables parallel calculation of multiple GraphProperty's via {@link Thread}s.
 * <p>The class uses a generic type to enable use for different types of values as calculation results.
 * This also ensures, that an appropriate type is chosen for each implementing class, since each property requires a value.
 * @author Fabian Grun
 * @param <T> The type of the value(s) calculated by the implementing classes.
 * @see BetweennessCentralityMeasure
 * @see Connectivity
 * @see Diameter
 * @see MinimumSpanningTree
 * @see ShortestPathList
 */
public interface GraphProperty<T> extends Runnable {
	
	/**
	 * Method to return the calculated value(s) of the Property.
	 * @return The value calculated by the {@link #run()} method.
	 */
	public T getValue();
	
	/**
	 * Method responsible for the actual calculation of the property value(s).
	 */
	public abstract void run();

	/**
	 * Method for printing the calculated result(s) as a message to the console.
	 */
	public void printToConsole();
}
