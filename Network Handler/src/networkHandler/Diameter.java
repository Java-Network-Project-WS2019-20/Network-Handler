package networkHandler;


import java.util.TreeSet;

public class Diameter implements GraphProperty<Double> {
	//	Attributes
	private	Graph			graph;
	private	Double			diameterValue;
	private	TreeSet<Path>	shortestPaths;
	private	boolean			connectivity;
	
	//	Constructor
	public			Diameter(Graph graph, TreeSet<Path> shortestPaths, boolean connectivity) {
		this.graph			= graph;
		this.shortestPaths	= shortestPaths;
		this.connectivity	= connectivity;
	}
	
	public	Double	getValue() {
		return this.diameterValue;
	}
	
	//	alternative getter method
	public	Double	getDiameter() {
		return this.getValue();
	}
	
	//	implementation of calculate method from GraphProperty Superclass
	public	void	run() {
		if(connectivity) {
			this.diameterValue = 0.0;
			this.shortestPaths.forEach(path ->	{
				if(path.getLength() > this.diameterValue) {
					this.diameterValue = path.getLength();
				}
			});
		}else {
			this.diameterValue = Double.POSITIVE_INFINITY;
		}
	}
	
	public	void	printToConsole() {
		System.out.print("The Diameter of the graph is " + getValue().toString() + ".\n"); 
	}
}
