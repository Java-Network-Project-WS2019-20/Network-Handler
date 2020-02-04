package networkHandler;

/**
 * Interface for all calculations. It defines what methods should be included by
 * the Classes for the calculations.
 * 
 * @author Fabian Grun
 * @param <T> data type of the class of the calculation
 */
public interface GraphProperty<T> extends Runnable {

	public T getValue();

	public abstract void run();

	public void printToConsole();
}
