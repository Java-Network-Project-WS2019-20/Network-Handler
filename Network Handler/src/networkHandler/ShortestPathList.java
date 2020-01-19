package networkHandler;

import java.util.ArrayList;

public class ShortestPathList extends GraphProperty<ArrayList<Path>>{
	//	Attribute
	private	ArrayList<ArrayList<ArrayList<Path>>>	sortedListOfPaths;
	
	//	Constructor
	public	ShortestPathList(Graph graph) {
		super(graph);
	}
	
	//	alternative getter method
	public ArrayList<Path> getListOfShortestPaths(){
		return this.getValue();
	}
	
	//	implementation of inherited abstract method form GraphProperty class
	public	void	calculate() {
		
	}
	
}
