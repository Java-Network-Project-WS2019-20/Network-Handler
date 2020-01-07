package networkHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

public class Graph {
	private ArrayList<Edge> edgeList;
	private ArrayList<Node> nodeList;
	private MultiValuedMap<Integer, ArrayList<Integer>> shortestPathsMap = new ArrayListValuedHashMap<>();
	private int numberOfNodes;
	private int numberOfEdges;
	private boolean connectivity;
	private double diameter;
	
	// Constructor
	Graph (ArrayList<Edge> EdgeList, ArrayList<Node> NodeList){
		this.edgeList = EdgeList;
		this.nodeList = NodeList;
		this.numberOfNodes = NodeList.size();
		this.numberOfEdges = EdgeList.size();
		this.connectivity = this.isGraphConnected();
		this.diameter = this.diameter();
		this.getAllShortestPaths();
	}
	
	// getter shortestPathsMap
	public MultiValuedMap<Integer, ArrayList<Integer>> getShortestPathsMap() { return shortestPathsMap; }

	public int getNodeCount() {
		return numberOfNodes;
	}
	
	public int getEdgeCount() {
		return numberOfEdges;
	}
	
	public boolean getConnectivity() {
		return connectivity;
	}
	
	public double getDiameter() {
		return diameter;
	}
	
	public void printAllNodes() {
		for (Node n : nodeList) {
			System.out.println(n.toString());
		}
	}
	
	public void printAllEdges() {
		StringBuilder sb = new StringBuilder();
		for (Edge e : edgeList) {
			sb.append("EdgeID: ").append(e.getEdgeID()).append("\n");		
		}
		System.out.println(sb);
	}
	
	// Method to calculate, which nodes are reachable
	// From a certain node v
	// Using the Depth First Search Algorithm
	public boolean[] dfsVisit(int v, boolean[] visited, LinkedList<Integer>[] adjListArray) {
		// Mark the current node as visited
		visited[v] = true;
		// repeat for all the nodes
		// adjacent to this node
		for (int x : adjListArray[v]) {
			if(!visited[x]) dfsVisit(x,visited,adjListArray);
		}
		return visited;
	}
	
	// Method to calculate, whether a undirected Graph is connected
	// Not minding the weight of the edges
	public boolean isGraphConnected() {

		// A linked list of all adjacent Nodes for each Node
		// Size of array is the number of Nodes
		LinkedList<Integer>[] adjListArray = new LinkedList[getNodeCount()];
		
		
		// Create a new list for each Node
		// so that adjacent nodes can be stored
		for (int i = 0; i < getNodeCount(); i++) {
		adjListArray[i] = new LinkedList<Integer>();
		}
		// Adds all edges to the nodes
		// Since graph is undirected, add an edge from source to target
		// as well as target to source
		for (Edge e : this.edgeList) {
			adjListArray[e.getSource()].add(e.getTarget());
			adjListArray[e.getTarget()].add(e.getSource());
		}
		
		// Mark all Nodes as not visited
		boolean[] visited = new boolean[getNodeCount()];
		// Calculate all reachable Nodes from Node 1
		// If one Node is not reachable, it means the graph
		// Is not connected
		dfsVisit(0,visited,adjListArray);
		for(int v = 0; v < getNodeCount(); v++) {
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
		double[][] connections = new double[getNodeCount()][getNodeCount()];
		//	Fill the adjacency matrix by iterating over the list of edges
		for(int i = 0; i < getEdgeCount(); i++) {
			//	Get necessary values (source Node/target Node/weight) from next edge
			Edge	edge	= edgeList.get(i);
			int		source	= edge.getSource();
			int		target	= edge.getTarget();
			double	weight	= edge.getWeight();
			//	Write values into adjacency matrix
			connections[source][target] = weight;
			connections[target][source] = weight;
		}
		/*	Initialize Map of unvisited Nodes 
		 *	Keys are the Map IDs
		 *	Values are the distances from the initial Node
		 */
		TreeMap<Integer, Double> unvisitedNodes = new TreeMap<>();
		
		/*	Fill Map 
		 *	values are infinity, except for the initial node, which has value 0
		 */
		for(int i = 0; i < getNodeCount(); i++) {
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
			for(int i = 0; i < getNodeCount(); i++) {
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
	
	//Method to get the Diameter of a Graph
	public double diameter() { 
		if(!isGraphConnected()){
			return Double.POSITIVE_INFINITY;
		}
		double maxShortestPath = 0;
		// calculate all the shortest paths in the Graph
		// and check which one has the highest distance
		for(int i = 0; i < getNodeCount(); i++) {
			for(int n = i; n < getNodeCount(); n++) {
				if(shortestPath(i,n) > maxShortestPath) {
					maxShortestPath = shortestPath(i,n);
				}
			}
		}
		return maxShortestPath;
	}
	
	// Calculate all shortestPaths and put them into a multiMap
	// return multiMap to forward them to the GraphWriter for further processing
	public MultiValuedMap<Integer, ArrayList<Integer>> getAllShortestPaths() {
		
		// MultiMap consists of:
		//	keys = source node ID's
		//	values = two ArrayLists containing:
		//		1. ArrayList = Target node ID's
		//		2. ArrayList = Shortest path calculation results
		
		for(int i = 0; i < getNodeCount(); i++) {
			for(int n = i; n < getNodeCount(); n++) {
				ArrayList<Integer> targetNodes = new ArrayList<Integer>();
			    ArrayList<Integer> spResults = new ArrayList<Integer>();
				
				targetNodes.add(n);
				spResults.add((int) shortestPath(i,n));
				
			    // Put both ArrayLists into multiMap for same key
				shortestPathsMap.put(i, targetNodes );
				shortestPathsMap.put(i, spResults);
			}
		}
		
		return shortestPathsMap;
	}

		
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Number of nodes: ").append(getNodeCount()).append("\n");
		sb.append("Number of egdes: ").append(getEdgeCount()).append("\n");
		sb.append("Is connected? ").append(isGraphConnected()).append("\n");
		return sb.toString();
	}
	
}
