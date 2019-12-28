package networkHandler;
import java.util.ArrayList;

//	Note: this class has a natural ordering that is inconsistent with equals.
public class Path implements Comparable<Path>{
	
//	Attributes
	
	/*	nodes (IDs) on the path 
	 *	in order from origin to destination
	 */
	private	ArrayList<Integer>	nodes;
	
	/*	edges (IDs) of the path
	 *	in order from origin to destination
	 *	empty if the nodes have no path to connect them
	 */
	private	ArrayList<Integer>	edges;
	
	/*	length of the path
	 *  infinity if no connection between nodes exists
	 */
	private	double 				length;
	
//	Constructors
	
	//	given all values
	Path(ArrayList<Integer> nodes, ArrayList<Integer> edges, double length){
		this.nodes = new ArrayList<Integer>();
		this.nodes.addAll(nodes);
		this.edges = new ArrayList<Integer>();
		this.edges.addAll(edges);
		this.length = length;
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
	
	//	number of Edges
	public	int		getNumberOfEdges() {
		return this.edges.size();
	}
	
	/*	i-th Node of path
	 * 	numbered from 1 to size()
	 */
	public	int		getNode(int i) {
		return this.nodes.get(i - 1);
	}
	
	/*	i-th Edge of path
	 *	numbered from 1 to size()
	 */
	public	int		getEdge(int i) {
		return this.edges.get(i - 1);
	}
	
	//	length of path
	public	double	getLength() {
		return length;
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
		if(this.length == that.length && this.getNumberOfEdges() == that.getNumberOfEdges() && this.getNumberOfNodes() == that.getNumberOfNodes()) {
			if(this.nodes.containsAll(that.nodes) && this.edges.containsAll(that.edges)) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	
}
