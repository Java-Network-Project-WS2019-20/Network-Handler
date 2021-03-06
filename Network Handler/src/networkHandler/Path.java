package networkHandler;
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
	
//	comparing methods
	
	/*	compare Paths by length
	 *	equality in this respect does not correspond to equality of Paths
	 */
	public	int		compareTo(Path that) {
		if(this.length == that.length) {
			return 0;
		}else {
			if(this.length < that.length) {
				return -1;
			}else {
				return 1;
			}
		}
	}
	
	//	method to check equality of Paths
	public	boolean	equals(Path that) {
		return this.length == that.length  && this.getNumberOfNodes() == that.getNumberOfNodes() && this.nodes.containsAll(that.nodes);
	}
	
}
