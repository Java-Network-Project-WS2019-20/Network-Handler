package networkHandler;

import java.util.ArrayList;

public class Diameter extends GraphProperty<Double> {
	//	Attributes
	private	ArrayList<Path>	shortestPaths;
	private	boolean			connectivity;
	
	//	Constructor
	public		Diameter(Graph graph, ArrayList<Path> shortestPaths) {
		super(graph);
		this.shortestPaths = shortestPaths;
	}
	
	//	alternative getter method
	public	double	getDiameter() {
		return this.getValue();
	}
	
	//	implementation of calculate method from GraphProperty Superclass
	public void calculate() {
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
