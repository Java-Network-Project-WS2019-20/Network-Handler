package networkHandler;

import java.util.Iterator;
import java.util.TreeSet;

public class BetweennessCentralityMeasure implements GraphProperty<Double>{
	//	Attribute
	private Graph			graph;
	private	TreeSet<Path>	shortestPaths;
	private int				nodeId;
	private	Double			betweennessCentralityMeasureValue;
	
	//	Constructor
	public			BetweennessCentralityMeasure(Graph graph, TreeSet<Path> shortestPaths, int nodeId) {
		this.graph								= graph;
		this.shortestPaths						= shortestPaths;
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
		
		double[][] countsOfAllPaths = new double[this.graph.getNodeCount()][this.graph.getNodeCount()];
		double[][] countsOfPathsContainingNode = new double[this.graph.getNodeCount()][this.graph.getNodeCount()];
		
		Iterator<Path> iterator = this.shortestPaths.iterator();
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
	}
	
	public	void	printToConsole() {
		System.out.print("The Betweenness Centrality Measure for Node v" + this.nodeId + " is " + getValue().toString() + ".\n");
	}
}
