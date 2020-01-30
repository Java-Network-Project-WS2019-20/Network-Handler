package networkHandler;


import java.util.TreeSet;

public class Diameter implements GraphProperty<Double> {
	//	Attributes
	private	Double				diameterValue;
	private	ShortestPathList	shortestPathList;
	private	Connectivity		connectivity;
	
	//	Constructor
	public			Diameter(ShortestPathList shortestPathList, Connectivity connectivity) {
		this.shortestPathList	= shortestPathList;
		this.connectivity		= connectivity;
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
		if(connectivity.getValue()) {
			this.diameterValue = 0.0;
			this.shortestPathList.getValue().forEach(path ->	{
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
