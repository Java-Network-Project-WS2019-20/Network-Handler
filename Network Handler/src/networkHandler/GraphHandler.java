package networkHandler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.TreeSet;

public class GraphHandler {
//	Attributes
	private	Graph 							graph;
	private	Connectivity					connectivity;
	private	Diameter						diameter;
	private	ShortestPath					shortestPath;
	private BetweennessCentralityMeasure	betweennessCentralityMeasure;
	private	ShortestPathList				shortestPathList;
	private	ArrayList<Double>				betweennessCentralityMeasureList;
	private	int								shortestPathInitialNodeId;
	private	int								shortestPathDestinationNodeId;
	private	int								betweennessCentralityMeasureNodeId;
	private final 							Logger mylog = LogManager.getLogger(GraphHandler.class);


	//	Constructor
	//	TODO choose one, first currently not used
	public	GraphHandler(Graph graph) {
		this.graph = graph;
	}
	
	public	GraphHandler(ArrayList<Edge> edgeList, ArrayList<Node> nodeList) {
		this.graph = new Graph(edgeList, nodeList);
	}
	
	public	void				calculateDiameter() {
		this.diameter = new Diameter(this.graph, this.shortestPathList.getValue(), this.connectivity.getValue());
		this.diameter.run();
		mylog.info("Diameter was calculated successfully");
	}
	
	public	void				calculateConnectivity() {
		this.connectivity = new Connectivity(this.graph);
		this.connectivity.run();
		mylog.info("Connectivity was calculated successfully");
	}
	
	public	void				calculateSingleShortestPath() {
		this.shortestPath = new ShortestPath(this.graph, this.shortestPathInitialNodeId, this.shortestPathDestinationNodeId);
		this.shortestPath.run();
		mylog.info("Shortest path between " + this.shortestPathInitialNodeId + " and" + this.shortestPathDestinationNodeId + " was calculated successfully");

	}
	
	public	void				calculateAllShortestPaths() {
		this.shortestPathList = new ShortestPathList(this.graph);
		this.shortestPathList.run();
		mylog.info("Shortest path between all nodes were calculated successfully");

	}
	
	public	void				calculateSingleBetweennessCentralityMeasure() {
		this.betweennessCentralityMeasure = new BetweennessCentralityMeasure(this.graph, this.shortestPathList.getValue(), this.betweennessCentralityMeasureNodeId);
		this.betweennessCentralityMeasure.run();
		mylog.info("Single betweenness centrality measure was calculated successfully");

	}
	
	public	void				calculateAllBetweennessCentralityMeasures() {
		this.betweennessCentralityMeasureList = new ArrayList<Double>();
		for(int i = 0; i < this.graph.getNodeCount(); i++) {
			BetweennessCentralityMeasure bcm = new BetweennessCentralityMeasure(this.graph, this.shortestPathList.getValue(), i);
			bcm.run();
			this.betweennessCentralityMeasureList.add(bcm.getValue());
		}
		mylog.info("All betweenness centrality measure was calculated successfully");


	}
	
	//	TODO: add threading
	public	void				runCalculations(CommandLineReader commandLineReader) {
		Thread	connectivityThread;
		Thread	diameterThread;
		Thread	allShortestPathsThread;
		Thread	allBetweennessCentralityMeasuresThread;
		Thread	singleShortestPathThread;
		Thread	singleBetweennessCentralityMeasureThread;
		if(commandLineReader.getFlagCreateOutputFile()) {
			this.connectivity = new Connectivity(this.graph);
			connectivityThread = new Thread(this.connectivity, "Connectivity Calculation");
			connectivityThread.run();
			this.shortestPathList = new ShortestPathList(this.graph);
			allShortestPathsThread = new Thread(this.shortestPathList, "Shortest Paths Calculation");
			allShortestPathsThread.run();
			this.calculateDiameter();
			this.calculateAllBetweennessCentralityMeasures();
		}else {
			if(	commandLineReader.getFlagConnectivity()
			||	commandLineReader.getFlagDiameter()
			)
			{
				this.calculateConnectivity();
			}
			if(	commandLineReader.getFlagAllShortestPaths()
			||	commandLineReader.getFlagDiameter()
			||	commandLineReader.getFlagBCM()
			)
			{
				this.calculateAllShortestPaths();
			}
			if(	commandLineReader.getFlagDiameter()
			)
			{
				this.calculateDiameter();
			}
			if(	commandLineReader.getFlagBCM()
			)
			{
				this.setBetweennessCentralityMeasureParameter(commandLineReader.getBcmNodeID());
				this.calculateSingleBetweennessCentralityMeasure();
			}
			if(	commandLineReader.getFlagShortestPathBetweenTwoNodes()
			||	!commandLineReader.getFlagAllShortestPaths()
			)
			{
				this.setSingleShortestPathParameters(commandLineReader.getSpIDone(), commandLineReader.getSpIDtwo());
				this.calculateSingleShortestPath();
			}
		}
	}
	
	public	void				setSingleShortestPathParameters(int initialNodeId, int destinationNodeId) {
		this.shortestPathInitialNodeId		= initialNodeId;
		this.shortestPathDestinationNodeId	= destinationNodeId;
	}
	
	public	void				setBetweennessCentralityMeasureParameter(int nodeId) {
		this.betweennessCentralityMeasureNodeId = nodeId;
	}
	
	public	Graph				getGraph() {
		return this.graph;
	}
	
	public	double				getDiameter() {
		return this.diameter.getValue();
	}
	
	public	boolean				getConnectivity() {
		return this.connectivity.getValue();
	}
	
	public	Path				getSingleShortestPath() {
		return this.shortestPath.getValue();
	}
	
	public	TreeSet<Path>		getShortestPathsList(){
		return this.shortestPathList.getValue();
	}
	
	public	double				getSingleBetweennessCentralityMeasure() {
		return this.betweennessCentralityMeasure.getValue();
	}
	
	public	ArrayList<Double>	getAllBetweennessCentralityMeasures(){
		return this.betweennessCentralityMeasureList;
	}
	

}
