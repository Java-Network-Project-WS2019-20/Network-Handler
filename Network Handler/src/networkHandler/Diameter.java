package networkHandler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * <p>This class is responsible for the calculation of the Diameter of a {@link Graph} given a {@link ShortestPathsList} and its {@link Connectivity}.
 * @author Krzysztof
 * @author Fabian Grun
 * @see GraphProperty
 */
public class Diameter implements GraphProperty<Double> {

	private Double diameterValue;
	private ShortestPathList shortestPathList;
	private Connectivity connectivity;
	private final Logger mylog = LogManager.getLogger(Diameter.class);



	public Diameter(ShortestPathList shortestPathList, Connectivity connectivity) {
		this.shortestPathList = shortestPathList;
		this.connectivity = connectivity;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Double getValue() {
		return this.diameterValue;
	}

	/**
	 * <p>{@inheritDoc}
	 * <p>In case of a {@link Connectivity} value 'false' for the {@link Graph}, the value will be set to Infinity. 
	 */
	public void run() {
		mylog.debug("Started calculation of Diameter.");
		if (connectivity.getValue()) {
			this.diameterValue = 0.0;
			this.shortestPathList.getValue().forEach(path -> {
				if (path.getLength() > this.diameterValue) {
					this.diameterValue = path.getLength();
				}
			});
		} else {
			this.diameterValue = Double.POSITIVE_INFINITY;
		}
		mylog.debug("Finished calculation of Diameter");
	}

	/**
	 * {@inheritDoc}
	 */
	public void printToConsole() {
		mylog.info("The Diameter of the graph is " + getValue().toString());
	}
}
