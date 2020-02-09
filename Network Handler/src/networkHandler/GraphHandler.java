package networkHandler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * <p>This class is responsible for managing all from {@link GraphProperty} derived classes and initiates their calculation on a {@link Graph}, which is handled in this class as well.
 * <p>It is also responsible for forwarding calls to the printing methods of the GraphProperties and calls to their value getters.
 * <p>To determine the necessary calculations it checks the flags provided by a {@link CommandLineReader} accordingly.
 * 
 * @author Fabian Grun
 * @author Khalid Butt
 * @see BetweennessCentralityMeasure
 * @see BetweennessCentralityMeasureList
 * @see Connectivity
 * @see Diameter
 * @see GraphProperty
 * @see MinimumSpanningTree
 * @see ShortestPathList
 */ 
public class GraphHandler {
	
	private Graph graph;
	private Connectivity connectivity;
	private Diameter diameter;
	private ShortestPathList shortestPathList;
	private BetweennessCentralityMeasureList betweennessCentralityMeasureList;
	private MinimumSpanningTree minimumSpanningTree;
	private CommandLineReader commandLineReader;
	private final Logger mylog = LogManager.getLogger(GraphHandler.class);

	public GraphHandler(ArrayList<Edge> edgeList, ArrayList<Node> nodeList, CommandLineReader commandLineReader) {
		this.graph = new Graph(edgeList, nodeList);
		this.commandLineReader = commandLineReader;
	}

	/**
	 * <p>This method is responsible for handling all calculations.
	 * <p>First it checks the flags provided by the commandLineReader and determines, which calculations are expected and also which might be dependent on others.
	 * <p>In case a flag for file creation is set, all properties are calculated, otherwise only those specified by the printing flags of the {@link CommandLineReader}.
	 * <p>Each {@link GraphProperty} is then calculated in its own {@link Thread} for efficiency, while making sure, dependencies are finished with their calculations before continuing to dependents.
	 */
	public void runCalculations() {
		Thread connectivityThread = new Thread();
		Thread shortestPathThread = new Thread();
		Thread diameterThread = new Thread();
		Thread betweennessCentralityMeasureThread = new Thread();
		Thread minimumSpanningTreeThread = new Thread();

		if (commandLineReader.getFlagCreateOutputFile()) {
			this.connectivity = new Connectivity(this.graph);
			connectivityThread = new Thread(this.connectivity, "Connectivity Calculation");
			connectivityThread.start();
			if (commandLineReader.getFlagShortestPathsTwoNodes()) {
				this.shortestPathList = new ShortestPathList(this.graph, commandLineReader.getFlagShortestPathsNoDuplicates(), true, commandLineReader.getShortestPathNodeID1(), commandLineReader.getShortestPathNodeID2());
			} else {
				this.shortestPathList = new ShortestPathList(this.graph, commandLineReader.getFlagShortestPathsNoDuplicates());
			}
			shortestPathThread = new Thread(this.shortestPathList, "Shortest Paths Calculation");
			shortestPathThread.start();
			try {
				shortestPathThread.join();
				connectivityThread.join();
			} catch (InterruptedException e) {
				mylog.error("Something went wrong: " +e.getMessage());
			}
			this.minimumSpanningTree = new MinimumSpanningTree(this.graph, this.connectivity);
			minimumSpanningTreeThread = new Thread(this.minimumSpanningTree, "Minimum Spanning Tree Calculation");
			minimumSpanningTreeThread.start();
			if (commandLineReader.getFlagBetweennessCentralityMeasureSingle()) {
				this.betweennessCentralityMeasureList = new BetweennessCentralityMeasureList(this.graph, this.shortestPathList, true, commandLineReader.getbetweennessCentralityMeasureNodeID());
			} else {
				this.betweennessCentralityMeasureList = new BetweennessCentralityMeasureList(this.graph, this.shortestPathList);
			}
			betweennessCentralityMeasureThread = new Thread(this.betweennessCentralityMeasureList, "Betweenness Centrality Measure Calculation");
			betweennessCentralityMeasureThread.start();
			this.diameter = new Diameter(this.shortestPathList, this.connectivity);
			diameterThread = new Thread(this.diameter, "Diameter Calculation");
			diameterThread.start();
		} else {
			if (commandLineReader.getFlagConnectivity() || commandLineReader.getFlagDiameter() || commandLineReader.getFlagMinimumSpanningTree()) {
				this.connectivity = new Connectivity(this.graph);
				connectivityThread = new Thread(this.connectivity, "Connectivity Calculation");
				connectivityThread.start();
			}
			if (commandLineReader.getFlagShortestPathsAll() || commandLineReader.getFlagDiameter() || commandLineReader.getFlagBetweennessCentralityMeasureSingle() || commandLineReader.getFlagBetweennessCentralityMeasureAll()) {
				if (commandLineReader.getFlagShortestPathsTwoNodes()) {
					this.shortestPathList = new ShortestPathList(this.graph, commandLineReader.getFlagShortestPathsNoDuplicates(), true, commandLineReader.getShortestPathNodeID1(), commandLineReader.getShortestPathNodeID2());
				} else {
					this.shortestPathList = new ShortestPathList(this.graph, commandLineReader.getFlagShortestPathsNoDuplicates());
				}
				shortestPathThread = new Thread(this.shortestPathList, "Shortest Paths Calculation");
				shortestPathThread.start();
			} else {
				if (commandLineReader.getFlagShortestPathsTwoNodes()) {
					this.shortestPathList = new ShortestPathList(this.graph, commandLineReader.getFlagShortestPathsNoDuplicates(), false, commandLineReader.getShortestPathNodeID1(), commandLineReader.getShortestPathNodeID2());
					shortestPathThread = new Thread(this.shortestPathList, "Shortest Paths Calculation");
					shortestPathThread.start();
				}
			}
			if (commandLineReader.getFlagMinimumSpanningTree()) {
				try {
					connectivityThread.join();
				} catch (InterruptedException e) {
					mylog.error("Something went wrong: " +e.getMessage());
				}
				minimumSpanningTree = new MinimumSpanningTree(this.graph, this.connectivity);
				minimumSpanningTreeThread = new Thread(minimumSpanningTree, "Minimum Spanning Tree Calculation");
				minimumSpanningTreeThread.start();
			}
			if (commandLineReader.getFlagDiameter()) {
				try {
					shortestPathThread.join();
					connectivityThread.join();
				} catch (InterruptedException e) {
					mylog.error("Something went wrong: " +e.getMessage());

				}
				this.diameter = new Diameter(this.shortestPathList, this.connectivity);
				diameterThread = new Thread(this.diameter, "Diameter Calculation");
				diameterThread.start();
			}
			if (commandLineReader.getFlagBetweennessCentralityMeasureSingle() || commandLineReader.getFlagBetweennessCentralityMeasureAll()) {
				try {
					shortestPathThread.join();
				} catch (InterruptedException e) {
					mylog.error("Something went wrong: " +e.getMessage());
				}
				if (commandLineReader.getFlagBetweennessCentralityMeasureAll() && commandLineReader.getFlagBetweennessCentralityMeasureSingle()) {
					this.betweennessCentralityMeasureList = new BetweennessCentralityMeasureList(this.graph, this.shortestPathList, true, commandLineReader.getbetweennessCentralityMeasureNodeID());
				} else {
					if (commandLineReader.getFlagBetweennessCentralityMeasureAll()) {
						this.betweennessCentralityMeasureList = new BetweennessCentralityMeasureList(this.graph, this.shortestPathList);
					} else {
						this.betweennessCentralityMeasureList = new BetweennessCentralityMeasureList(this.graph, this.shortestPathList, false, commandLineReader.getbetweennessCentralityMeasureNodeID());
					}
				}
				betweennessCentralityMeasureThread = new Thread(this.betweennessCentralityMeasureList, "Betweenness Centrality Measure Calculation");
				betweennessCentralityMeasureThread.start();
			}
		}
		try {
			connectivityThread.join();
			shortestPathThread.join();
			minimumSpanningTreeThread.join();
			diameterThread.join();
			betweennessCentralityMeasureThread.join();
		} catch (InterruptedException e) {
			mylog.error("Something went wrong: " +e.getMessage());
		}
	}

	public Graph getGraph() {
		return this.graph;
	}

	public double getDiameterValue() {
		return this.diameter.getValue();
	}

	public boolean getConnectivityValue() {
		return this.connectivity.getValue();
	}

	public TreeSet<Path> getShortestPathsListAllValue() {
		return this.shortestPathList.getValue();
	}

	public ArrayList<BetweennessCentralityMeasure> getAllBetweennessCentralityMeasuresValue() {
		return this.betweennessCentralityMeasureList.getValue();
	}

	public ArrayList<Edge> getMinimumSpanningTreeValue() {
		return this.minimumSpanningTree.getValue();
	}

	public void printToConsoleGraph() {
		graph.printToConsole();
	}

	public void printToConsoleConnectivity() {
		connectivity.printToConsole();
	}

	public void printToConsoleDiameter() {
		diameter.printToConsole();
	}

	public void printToConsoleShortestPathListAll() {
		shortestPathList.printToConsole();
	}

	public void printToConsoleShortestPathListTwoNodes() {
		shortestPathList.printToConsoleTwoNodes();
	}

	public void printToConsoleBetweennessCentralityMeasureList() {
		betweennessCentralityMeasureList.printToConsole();
	}

	public void printToConsoleBetweennessCentralityMeasureSingle() {
		betweennessCentralityMeasureList.printToConsoleSingle();
	}

	public void printToConsoleMinimumSpanningTree() {
		minimumSpanningTree.printToConsole();
	}
}
