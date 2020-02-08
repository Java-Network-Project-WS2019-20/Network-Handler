package networkHandler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Manages all the calculations. What should be calculated is determined by a
 * flag from the {@link CommandLineReader}. Also manages the print outs of the
 * calculations to the console.
 * 
 * @author Fabian Grun
 * @author Khalid Butt
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

	// TODO: error handling/logging
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
				this.shortestPathList = new ShortestPathList(this.graph,
						commandLineReader.getFlagShortestPathsNoDuplicates(), true, commandLineReader.getSpIDone(),
						commandLineReader.getSpIDtwo());
			} else {
				this.shortestPathList = new ShortestPathList(this.graph,
						commandLineReader.getFlagShortestPathsNoDuplicates());
			}
			shortestPathThread = new Thread(this.shortestPathList, "Shortest Paths Calculation");
			shortestPathThread.start();

			try {
				shortestPathThread.join();
				connectivityThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block

				mylog.error("Something went wrong: " +e.getMessage());

//				e.printStackTrace();
			}

			this.minimumSpanningTree = new MinimumSpanningTree(this.graph, this.connectivity);
			minimumSpanningTreeThread = new Thread(this.minimumSpanningTree, "Minimum Spanning Tree Calculation");
			minimumSpanningTreeThread.start();

			if (commandLineReader.getFlagBCMSingle()) {
				this.betweennessCentralityMeasureList = new BetweennessCentralityMeasureList(this.graph,
						this.shortestPathList, true, commandLineReader.getBcmNodeID());
			} else {
				this.betweennessCentralityMeasureList = new BetweennessCentralityMeasureList(this.graph,
						this.shortestPathList);
			}
			betweennessCentralityMeasureThread = new Thread(this.betweennessCentralityMeasureList,
					"Betweenness Centrality Measure Calculation");
			betweennessCentralityMeasureThread.start();

			this.diameter = new Diameter(this.shortestPathList, this.connectivity);
			diameterThread = new Thread(this.diameter, "Diameter Calculation");
			diameterThread.start();

		} else {
			// calculate connectivity?
			if (commandLineReader.getFlagConnectivity() || commandLineReader.getFlagDiameter()
					|| commandLineReader.getFlagMinimumSpanningTree()) {
				this.connectivity = new Connectivity(this.graph);
				connectivityThread = new Thread(this.connectivity, "Connectivity Calculation");
				connectivityThread.start();
			}

			// calculate single/all shortest paths?
			if (commandLineReader.getFlagShortestPathsAll() || commandLineReader.getFlagDiameter()
					|| commandLineReader.getFlagBCMSingle() || commandLineReader.getFlagBCMAll()) {
				if (commandLineReader.getFlagShortestPathsTwoNodes()) {
					this.shortestPathList = new ShortestPathList(this.graph,
							commandLineReader.getFlagShortestPathsNoDuplicates(), true, commandLineReader.getSpIDone(),
							commandLineReader.getSpIDtwo());
				} else {
					this.shortestPathList = new ShortestPathList(this.graph,
							commandLineReader.getFlagShortestPathsNoDuplicates());
				}
				shortestPathThread = new Thread(this.shortestPathList, "Shortest Paths Calculation");
				shortestPathThread.start();
			} else {
				if (commandLineReader.getFlagShortestPathsTwoNodes()) {
					this.shortestPathList = new ShortestPathList(this.graph,
							commandLineReader.getFlagShortestPathsNoDuplicates(), false, commandLineReader.getSpIDone(),
							commandLineReader.getSpIDtwo());
					shortestPathThread = new Thread(this.shortestPathList, "Shortest Paths Calculation");
					shortestPathThread.start();
				}
			}

			// calculate minimum spanning tree?
			if (commandLineReader.getFlagMinimumSpanningTree()) {
				try {
					connectivityThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					mylog.error("Something went wrong: " +e.getMessage());

//					e.printStackTrace();
				}
				minimumSpanningTree = new MinimumSpanningTree(this.graph, this.connectivity);
				minimumSpanningTreeThread = new Thread(minimumSpanningTree, "Minimum Spanning Tree Calculation");
				minimumSpanningTreeThread.start();
			}
			// calculate diameter?
			if (commandLineReader.getFlagDiameter()) {
				try {
					shortestPathThread.join();
					connectivityThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					mylog.error("Something went wrong: " +e.getMessage());

//					e.printStackTrace();
				}

				this.diameter = new Diameter(this.shortestPathList, this.connectivity);
				diameterThread = new Thread(this.diameter, "Diameter Calculation");
				diameterThread.start();
			}

			// calculate single/all betweenness centrality measure(s)?
			if (commandLineReader.getFlagBCMSingle() || commandLineReader.getFlagBCMAll()) {
				try {
					shortestPathThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
					mylog.error("Something went wrong: " +e.getMessage());

				}
				if (commandLineReader.getFlagBCMAll() && commandLineReader.getFlagBCMSingle()) {
					this.betweennessCentralityMeasureList = new BetweennessCentralityMeasureList(this.graph,
							this.shortestPathList, true, commandLineReader.getBcmNodeID());
				} else {
					if (commandLineReader.getFlagBCMAll()) {
						this.betweennessCentralityMeasureList = new BetweennessCentralityMeasureList(this.graph,
								this.shortestPathList);
					} else {
						this.betweennessCentralityMeasureList = new BetweennessCentralityMeasureList(this.graph,
								this.shortestPathList, false, commandLineReader.getBcmNodeID());
					}
				}
				betweennessCentralityMeasureThread = new Thread(this.betweennessCentralityMeasureList,
						"Betweenness Centrality Measure Calculation");
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
			// TODO Auto-generated catch block
			mylog.error("Something went wrong: " +e.getMessage());

//			e.printStackTrace();
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
