package networkHandler;
import java.util.ArrayList;

public class Path {
	
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
		this.nodes.addAll(nodes);
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
	
	
}
