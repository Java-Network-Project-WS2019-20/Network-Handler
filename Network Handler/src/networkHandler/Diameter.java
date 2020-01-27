package networkHandler;


import java.util.TreeSet;

public class Diameter extends GraphProperty<Double> {
	//	Attributes
	private	TreeSet<Path>	shortestPaths;
	private	boolean			connectivity;
	
	//	Constructor
	public		Diameter(Graph graph, TreeSet<Path> shortestPaths, boolean connectivity) {
		super(graph);
		this.shortestPaths = shortestPaths;
		this.connectivity = connectivity;
	}
	
	//	alternative getter method
	public	double	getDiameter() {
		return this.getValue();
	}
	
	//	implementation of calculate method from GraphProperty Superclass
	public void run() {
		if(connectivity) {
			this.value = 0.0;
			this.shortestPaths.forEach(path ->	{
				if(path.getLength() > this.value) {
					this.value = path.getLength();
				}
			});
		}else {
			this.value = Double.POSITIVE_INFINITY;
		}
	}
}
