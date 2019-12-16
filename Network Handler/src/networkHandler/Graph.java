package networkHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.TreeMap;

public class Graph {
	private ArrayList<Edge> EdgeList = new ArrayList<Edge>();
	private ArrayList<Node> NodeList = new ArrayList<Node>();
	int ESize; // Number of Edges
	int NSize; // Number of Nodes
	
	LinkedList<Integer>[] adjListArray; // A linked list of all adjacent Nodes for each Node
	
	// Constructor
	Graph (ArrayList<Edge> EdgeList, ArrayList<Node> NodeList){
		this.EdgeList = EdgeList;
		this.NodeList = NodeList;
		
		ESize = EdgeList.size();
		NSize = NodeList.size();
	}
	
	public int getNodeListSize() {
		return NSize;
	}
	
	public int getEdgeListSize() {
		return ESize;
	}
	
	public void PrintAllNodes() {
		for (Node n : NodeList) {
			System.out.println(n.toString());
		}
	}
	
	public void PrintAllEdges() {
		StringBuilder sb = new StringBuilder();
		for (Edge e : EdgeList) {
			sb.append("EdgeID: ").append(e.getEdgeID()).append("\n");		
		}
		System.out.println(sb);
	}
	
	
	// Method to calculate, which nodes are reachable
	// From a certain node v
	// Using the Depth First Search Algorithm
	public boolean[] DFSVisit(int v, boolean[] visited) {
		// Mark the current node as visited
		visited[v] = true;
		// repeat for all the nodes
		// adjacent to this node
		for (int x : adjListArray[v]) {
			if(!visited[x]) DFSVisit(x,visited);
		}
		return visited;
	}
	
	// Method to calculate, whether a undirected Graph is connected
	// Not minding the weight of the edges
	public boolean IsGraphConnected() {
		// Size of array is the number of Nodes
		adjListArray = new LinkedList[NSize];
		
		// Create a new list for each Node
		// so that adjacent nodes can be stored
		for (int i = 0; i < NSize; i++) {
		adjListArray[i] = new LinkedList<Integer>();
		}
		// Adds all edges to the nodes
		// Since graph is undirected, add an edge from source to target
		// as well as target to source
		for (Edge e : this.EdgeList) {
			adjListArray[e.getSource()].add(e.getTarget());
			adjListArray[e.getTarget()].add(e.getSource());
		}
		
		// Mark all Nodes as not visited
		boolean[] visited = new boolean[NSize];
		// Calculate all reachable Nodes from Node 1
		// If one Node is not reachable, it means the graph
		// Is not connected
		DFSVisit(0,visited);
		for(int v = 0; v < NSize; v++) {
			if(!visited[v])
				return false;
		}
		return true;
	}
	
	/*	Method to calculate the shortest path between two given Nodes by using Dijkstra's algorithm.
	 *	Returns the length of the path as a double value
	 *	Returns Infinity, if no path exists
	*/
	public double shortestPath(int initialNodeId, int destinationNodeId) {
		//	Initialize an adjacency matrix for the graph
		double[][] connections = new double[getNodeListSize()][getNodeListSize()];
		//	Fill the adjacency matrix by iterating over the list of edges
		for(int i = 0; i < getEdgeListSize(); i++) {
			//	Get necessary values (source Node/target Node/weight) from next edge
			Edge	edge	= EdgeList.get(i);
			int		source	= edge.getSource();
			int		target	= edge.getTarget();
			double	weight	= (double) edge.getWeight();		
			//	Write values into adjacency matrix
			connections[source][target] = weight;
			connections[target][source] = weight;
		}
		/*	Initialize Map of unvisited Nodes 
		 *	Keys are the Map IDs
		 *	Values are the distances from the initial Node
		 */
		TreeMap<Integer, Double> unvisitedNodes = new TreeMap<Integer, Double>();
		
		/*	Fill Map 
		 *	values are infinity, except for the initial node, which has value 0
		 */
		for(int i = 0; i < getNodeListSize(); i++) {
			if(i == initialNodeId) {
				unvisitedNodes.put(i, (double)0);
			}else {
				unvisitedNodes.put(i, Double.POSITIVE_INFINITY);
			}
		}
		
		//	initialize current node
		int currentNodeId = initialNodeId;
		
		/*	run trough Dijkstra's Algorithm until either
		 *  I.	The destination Node is the current Node
		 *  II.	There are no more Nodes to check (no path possible)
		 */
		while(currentNodeId != destinationNodeId && unvisitedNodes.get(currentNodeId) != Double.POSITIVE_INFINITY) {
			//	iterate over the adjacency matrix to find connected nodes
			for(int i = 0; i < getNodeListSize(); i++) {
				//	check whether a node is connected and unvisited
				if((connections[currentNodeId][i] > 0) && unvisitedNodes.containsKey(i)) {
					// 	calculate tentative distance to neighbor node
					double tentativeDistance = unvisitedNodes.get(currentNodeId) + connections[currentNodeId][i];
					//	compare current distance of neighbor node with tentative distance over current node
					if(tentativeDistance < unvisitedNodes.get(i)) {
						//	if tentative distance is smaller than current distance, replace current distance
						unvisitedNodes.put(i, tentativeDistance);
					}
				}
			}
			//	remove current node from Map
			unvisitedNodes.remove(currentNodeId);
			//	search next node, which has the smallest current distance
			
			//	initialize "iterator"
			int	nextNodeId = unvisitedNodes.firstKey();
			currentNodeId	= nextNodeId;
			//	iterate over the map keys
			for(int i = 0; i < unvisitedNodes.size() - 1; i++) {
				//	set next Node (to compare) to next higher key
				nextNodeId = unvisitedNodes.higherKey(nextNodeId);	
				//	compare values of next Node to current (smallest distance) node
				if(unvisitedNodes.get(currentNodeId) > unvisitedNodes.get(nextNodeId)) {
					//	replace current node by node with smaller distance
					currentNodeId = nextNodeId;
				}
			}
			
		}
		//	return the distance of the destination Node
		return unvisitedNodes.get(destinationNodeId);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Number of nodes: ").append(getNodeListSize()).append("\n");
		sb.append("Number of egdes: ").append(getEdgeListSize()).append("\n");
		sb.append("Is connected? ").append(IsGraphConnected()).append("\n");
		return sb.toString();
	}
	
}
