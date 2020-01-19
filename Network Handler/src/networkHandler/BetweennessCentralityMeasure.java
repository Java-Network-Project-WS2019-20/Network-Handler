package networkHandler;

import java.util.Iterator;
import java.util.TreeSet;

public class BetweennessCentralityMeasure extends GraphProperty<Double>{
	//	Attribute
	private	TreeSet<Path>	shortestPaths;
	private int				nodeId;
	
	//	Constructor
	public			BetweennessCentralityMeasure(Graph graph, TreeSet<Path> shortestPaths, int nodeId) {
		super(graph);
		this.shortestPaths	= shortestPaths;
		this.value			= 0.0;
		this.nodeId			= nodeId;
	}
	
	//	alternative getter method
	public double 	getBetweennessCentralityMeasure() {
		return this.getValue();
	}
	
	//	implementation of calculate from GraphProperty Superclass
	//	TODO: comments
	public	void	calculate() {
		
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
						this.value = this.value + (countsOfPathsContainingNode[i][j] / countsOfAllPaths[i][j]);
					}
				}
			}
		}
	}
}
