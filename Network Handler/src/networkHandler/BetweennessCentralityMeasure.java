package networkHandler;

import java.util.Iterator;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class BetweennessCentralityMeasure implements GraphProperty<Double>{
	//	Attribute
	private Graph				graph;
	private	ShortestPathList	shortestPathList;
	private int					nodeId;
	private	Double				betweennessCentralityMeasureValue;
	private	boolean				successfulCalculation;
	private final	Logger		mylog = LogManager.getLogger(BetweennessCentralityMeasure.class);
	//	Constructor
	public			BetweennessCentralityMeasure(Graph graph, ShortestPathList shortestPathList, int nodeId) {
		this.graph								= graph;
		this.shortestPathList					= shortestPathList;
		this.betweennessCentralityMeasureValue	= 0.0;
		this.nodeId								= nodeId;
	}
	
	//	implementation of getValue method
	public Double	getValue() {
		return this.betweennessCentralityMeasureValue;
	}
	
	//	alternative getter method
	public Double 	getBetweennessCentralityMeasure() {
		return this.getValue();
	}
	
	//	implementation of calculate from GraphProperty Superclass
	//	TODO: comments
	public	void	run() {
		if(this.nodeId > -1 && this.nodeId < this.graph.getNodeCount()) {
			mylog.info("Started calculation for BCM of Node n" + nodeId + ".");
			double[][] countsOfAllPaths = new double[this.graph.getNodeCount()][this.graph.getNodeCount()];
			double[][] countsOfPathsContainingNode = new double[this.graph.getNodeCount()][this.graph.getNodeCount()];
			
			Iterator<Path> iterator = this.shortestPathList.getValue().iterator();
			while(iterator.hasNext()) {
				Path path = iterator.next();
				countsOfAllPaths[path.getOriginNode()][path.getDestinationNode()]++;
				if(path.contains(nodeId)) {
					countsOfPathsContainingNode[path.getOriginNode()][path.getDestinationNode()]++;
				}
			}
			
			for(int i = 0; i < this.graph.getNodeCount(); i++) {
				if(i != nodeId) {
					for(int j = i + 1; j < this.graph.getNodeCount(); j++) {
						if(j != nodeId) {
							this.betweennessCentralityMeasureValue = this.betweennessCentralityMeasureValue + (countsOfPathsContainingNode[i][j] / countsOfAllPaths[i][j]);
						}
					}
				}
			}
			this.successfulCalculation = true;
		}else {
			this.successfulCalculation = false;
		}
	}
	
		public	void	printToConsole() {
		if(this.successfulCalculation) {
			System.out.print("The Betweenness Centrality Measure for Node n" + this.nodeId + " is " + getValue().toString() + ".\n");
		}else {
			System.out.print("Calculation for Node n" + this.nodeId +" is not possible. Node does not exist in given graph.\n");
		}
	}
}
