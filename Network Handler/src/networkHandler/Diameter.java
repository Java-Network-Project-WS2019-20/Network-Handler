package networkHandler;

/**
 * Calculates the Diameter of a {@link Graph}
 * 
 * @author Krzysztof
 */
public class Diameter implements GraphProperty<Double> {

	private Double diameterValue;
	private ShortestPathList shortestPathList;
	private Connectivity connectivity;

	public Diameter(ShortestPathList shortestPathList, Connectivity connectivity) {
		this.shortestPathList = shortestPathList;
		this.connectivity = connectivity;
	}

	public Double getValue() {
		return this.diameterValue;
	}

	public void run() {
		if (connectivity.getValue()) {
			this.diameterValue = 0.0;
			// check which shortest path is the biggest resp. longest
			this.shortestPathList.getValue().forEach(path -> {
				if (path.getLength() > this.diameterValue) {
					this.diameterValue = path.getLength();
				}
			});
		} else {
			this.diameterValue = Double.POSITIVE_INFINITY;
		}
	}

	public void printToConsole() {
		System.out.print("The Diameter of the graph is " + getValue().toString() + ".\n");
	}
}
