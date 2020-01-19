package networkHandler;

import java.util.ArrayList;

public class BetweennessCentralityMeasure extends GraphProperty<Double>{
	//	Attribute
	private	ArrayList<Path>	shortestPaths;
	
	//	Constructor
	public	BetweennessCentralityMeasure(Graph graph, ArrayList<Path> shortestPaths) {
		super(graph);
		this.shortestPaths = shortestPaths;
	}
	
	//	implementation of calculate from GraphProperty Superclass
	public	void	calculate() {
		
	}
}
