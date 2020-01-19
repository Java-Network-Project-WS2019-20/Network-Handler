package networkHandler;

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
	
//	Constructor
	public	GraphHandler(Graph graph) {
		this.graph = graph;
	}
	
	public	void				calculateDiameter() {
		this.diameter = new Diameter(this.graph, this.shortestPathList.getValue(), this.connectivity.getValue());
		this.diameter.calculate();
	}
	
	public	void				calculateConnectivity() {
		this.connectivity = new Connectivity(this.graph);
		this.connectivity.calculate();
	}
	
	public	void				calculateSingleShortestPath() {
		this.shortestPath = new ShortestPath(this.graph, this.shortestPathInitialNodeId, this.shortestPathDestinationNodeId);
		this.shortestPath.calculate();
	}
	
	public	void				calculateAllShortestPaths() {
		this.shortestPathList = new ShortestPathList(this.graph);
		this.shortestPathList.calculate();
	}
	
	public	void				calculateSingleBetweennessCentralityMeasure() {
		this.betweennessCentralityMeasure = new BetweennessCentralityMeasure(this.graph, this.shortestPathList.getValue(), this.betweennessCentralityMeasureNodeId);
		this.betweennessCentralityMeasure.calculate();
	}
	
	public	void				calculateAllBetweennessCentralityMeasures() {
		this.betweennessCentralityMeasureList = new ArrayList<Double>();
		for(int i = 0; i < this.graph.getNodeCount(); i++) {
			BetweennessCentralityMeasure bcm = new BetweennessCentralityMeasure(this.graph, this.shortestPathList.getValue(), i);
			bcm.calculate();
			this.betweennessCentralityMeasureList.add(bcm.getValue());
		}
	}
	
	public	void				calculateAllGraphProperties() {
		this.calculateConnectivity();
		this.calculateAllShortestPaths();
		this.calculateDiameter();
		this.calculateAllBetweennessCentralityMeasures();
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
