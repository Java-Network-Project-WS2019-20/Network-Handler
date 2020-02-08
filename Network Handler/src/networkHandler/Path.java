package networkHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;

//	Note: this class has a natural ordering that is inconsistent with equals.
public class Path implements Comparable<Path>{
	
//	Attributes
	
	/*	nodes (IDs) on the path 
	 *	in order from origin to destination
	 */
	private	ArrayList<Integer>	nodes;
	
	/*	length of the path
	 *  infinity if no connection between nodes exists
	 */
	private	double 				length;

	private final Logger mylog = LogManager.getLogger(Path.class);


//	Constructors
	
	//	given all values
	public			Path(ArrayList<Integer> nodes, /*ArrayList<Integer> edges,*/ double length){
		this.nodes = new ArrayList<Integer>();
		this.nodes.addAll(nodes);
		this.length = length;
	}
	
	//	copy
	public			Path(Path that) {
		this.nodes = new ArrayList<Integer>();
		this.nodes.addAll(that.nodes);
		this.length = that.length;
	}
	
//	getter methods
	
	//	origin Node
	public	int		getOriginNode() {
		return this.nodes.get(0);
	}
	
	//	destination Node
	public	int		getDestinationNode() {
		return this.nodes.get(this.nodes.size() - 1);
	}
	
	//	number of Nodes
	public	int		getNumberOfNodes() {
		return this.nodes.size();
	}
	
	/*	i-th Node of path
	 * 	numbered from 0 to size()-1
	 */
	public	int		getNode(int i) {
		return this.nodes.get(i);
	}
	
	//	length of path
	public	double	getLength() {
		return length;
	}

//	setter methods
	
	//	set length
	public	void	setLength(double length) {
		this.length = length;
	}
	
	//	extend the path with another node
	public	void	extend(double length, int NodeId) {
		//	set new path length
		this.setLength(length);
		//	add Node ID to list
		this.nodes.add(NodeId);
	}
	
//	check for containing a specific Node
	public boolean	contains(int NodeId) {
		return this.nodes.contains(NodeId);
		
	}
	
//	comparing methods
	
	/*	compare Paths by length
	 *	equality in this respect does not correspond to equality of Paths
	 */
	public	int		compareTo(Path that) {
		if(this.getOriginNode() == that.getOriginNode() && this.getDestinationNode() == that.getDestinationNode()) {
			if(this.getNumberOfNodes() < that.getNumberOfNodes()) {
				return -1;
			}else if (this.getNumberOfNodes() > that.getNumberOfNodes()){
				return 1;
			}else {
				for(int i = 0; i < this.getNumberOfNodes(); i++) {
					if(this.getNode(i) < that.getNode(i)) {
						return -1;
					}else if(this.getNode(i) > that.getNode(i)){
						return 1;
					}
				}
			}
			return 0;
		}else {
			if(this.getOriginNode() == that.getOriginNode()) {
				if(this.getDestinationNode() < that.getDestinationNode()) {
					return -1;
				}else {
					return 1;
				}
			}else{
				if(this.getOriginNode() < that.getOriginNode()) {
					return -1;
				}else {
					return 1;
				}
			}
		}
	}
	
	//	method to check equality of Paths
	public	boolean	equals(Path that) {
		return (this.length == that.length  && this.getNumberOfNodes() == that.getNumberOfNodes() && this.nodes.containsAll(that.nodes));
	}
	
//	printing
	public	void	printToConsole() {

//		System.out.print("Shortest path between n" + getOriginNode() + " and n" + getDestinationNode() + ": {");
		String x = "Shortest path between n" + getOriginNode() + " and n" + getDestinationNode() + ": {";
		for(int i = 0; i < getNumberOfNodes() - 1; i++) {
			System.out.print("n" + getNode(i) + ",");
			x = x + "n" + getNode(i) + ",";
		}
		x = x + "n" + getDestinationNode() + "}, length: " + getLength() + "\n";
//		System.out.print("n" + getDestinationNode() + "}, length: " + getLength() + "\n");
//		System.out.println(x);
		mylog.info(x);
	}
}
