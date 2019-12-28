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

	
	// Constructor
	Graph (ArrayList<Edge> EdgeList, ArrayList<Node> NodeList){
		this.edgeList = EdgeList;
		this.nodeList = NodeList;
	}
	
	public int getNodeCount() {
		return nodeList.size();
	}
	
	public int getEdgeCount() {
		return edgeList.size();
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
	
	
	
	//	Method to calculate the shortest path between two given Nodes by using Dijkstra's algorithm.
	public Path shortestPath(int initialNodeId, int destinationNodeId) {
		
		/*	Initialize an adjacency matrix for the graph
		 * 	This matrix stores the Edge IDs of corresponding Nodes
		 * 	A positive (>= 0) entry shows an existing Edge between Nodes
		 */
		int[][] connections = new int[getNodeCount()][getNodeCount()];
		
		/*	Fill the matrix with value -1
		 * 	This step of initialization is necessary, since the standard initialization value of 0 would be a valid entry
		 * 	-1 is therefore used to mark "empty" entries
		 */
		for(int i = 0; i < getNodeCount(); i++) {
			for(int j = 0; j < getNodeCount(); j++) {
				connections[i][j] = -1;
			}
		}
		
		//	Fill the adjacency matrix by iterating over the list of edges
		for(int i = 0; i < getEdgeCount(); i++) {
			//	Get necessary values (source Node/target Node/weight) from next edge
			Edge	edge	= edgeList.get(i);
			int		source	= edge.getSource();
			int		target	= edge.getTarget();
			//	Write values into adjacency matrix
			connections[source][target] = i;
			connections[target][source] = i;
		}
		
		/*	Initialize Map of unvisited Nodes 
		 *	Keys are the Map IDs
		 *	Values are the distances from the initial Node
		 */
		TreeMap<Integer, Double> unvisitedNodes = new TreeMap<>();
		
		/*	Fill Map 
		 *	values are set to infinity, except for the initial node, which has value 0
		 */
		for(int i = 0; i < getNodeCount(); i++) {
			if(i == initialNodeId) {
				unvisitedNodes.put(i, (double)0);
			}else {
				unvisitedNodes.put(i, Double.POSITIVE_INFINITY);
			}
		}
		
		/*	Initialize Map of parent Nodes
		 * 	The parent Node means the Node right before on the path
		 * 	Keys are the Nodes
		 * 	Values are the parent Nodes
		 */
		TreeMap<Integer,Integer> parentNodes = new TreeMap<>();
		
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
				if((connections[currentNodeId][i] > -1) && unvisitedNodes.containsKey(i)) {
					//	get connecting edge
					Edge currentEdge = edgeList.get(connections[currentNodeId][i]);
					//	get weight of edge
					double weight = currentEdge.getWeight();
					// 	calculate tentative distance to neighbor node
					double tentativeDistance = unvisitedNodes.get(currentNodeId) + weight;
					//	compare current distance of neighbor node with tentative distance over current node
					if(tentativeDistance < unvisitedNodes.get(i)) {
						//	if tentative distance is smaller than current distance, replace current distance
						unvisitedNodes.put(i, tentativeDistance);
						//	add/replace entry for parent node
						parentNodes.put(i, currentNodeId);
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
		
		//	create return value
		
		//	initialize list of Nodes on the path represented by their IDs
		ArrayList<Integer> NodesOnPath = new ArrayList<Integer>();
		//	initialize list of Edges on the path represented by their IDs
		ArrayList<Integer> EdgesOnPath = new ArrayList<Integer>();
		//	get the calculated length of the path
		double length = unvisitedNodes.get(destinationNodeId);
		/*	check the length
		 * 	a value of infinity means there exists no path between the nodes
		 */
		if(length != Double.POSITIVE_INFINITY) {
			//	if the nodes have a path between them, a corresponding path object is created
			
			//	create the list of Nodes on the path
			
			/*	initialize a temporary list of the nodes
			 * 	since the parent node represents the Node before a Node on a path, the path is build backwards first
			 */
			ArrayList<Integer> NodesOnPathTemp = new ArrayList<Integer>();
			//	add the destination node as start
			NodesOnPathTemp.add(destinationNodeId);
			//	initialize the next node to check for a parent node
			int nextNode = destinationNodeId;
			//	follow parent nodes to the initial node
			while(nextNode != initialNodeId) {
				//	get parent node Id
				int parentId = parentNodes.get(nextNode);
				//	add parent node to list of nodes on path
				NodesOnPathTemp.add(parentId);
				//	set next Node to current parent Node
				nextNode = parentId;
			}
			//	fill list with Node IDs in correct order
			for(int i = NodesOnPathTemp.size() - 1; i >= 0; i--) {
				NodesOnPath.add(NodesOnPathTemp.get(i));
			}
			
			//	create list of edges on the path
			
			//	iterate over the list of nodes to get corresponding edges
			for(int i = 0; i < NodesOnPath.size() - 1; i++) {
				//	get Node IDs
				int nodeId1 = NodesOnPath.get(i);
				int nodeId2 = NodesOnPath.get(i+1);
				//	find Edge ID in adjacency matrix
				int edgeId = connections[nodeId1][nodeId2];
				//	add edge to list 
				EdgesOnPath.add(edgeId);
			}
			

		}else {
			/*	if there is no path between the nodes,
			 *	the list of Nodes only contains the initial and the destination Node.
			 *	The list of Edges is empty.
			 *	The length is set to infinity.
			 */

			//	add initial Node ID to the list
			NodesOnPath.add(initialNodeId);
			//	add destination Node ID to the list
			NodesOnPath.add(destinationNodeId);
			
		}
		//	create Path Object
		Path path = new Path(NodesOnPath, EdgesOnPath, length);
		//	return result
		return path;
		
	}
	
	/*	Method to calculate the shortest paths between an initial Node and all other Nodes by using Dijkstra's algorithm
	 * 	Returns a list of the paths, sorted by destination Node IDs
	 */
	public ArrayList<Path> shortestPaths(int initialNodeId){

		/*	Initialize an adjacency matrix for the graph
		 * 	This matrix stores the Edge IDs of corresponding Nodes
		 * 	A positive (>= 0) entry shows an existing Edge between Nodes
		 */
		int[][] connections = new int[getNodeCount()][getNodeCount()];
		
		/*	Fill the matrix with value -1
		 * 	This step of initialization is necessary, since the standard initialization value of 0 would be a valid entry
		 * 	-1 is therefore used to mark "empty" entries
		 */
		for(int i = 0; i < getNodeCount(); i++) {
			for(int j = 0; j < getNodeCount(); j++) {
				connections[i][j] = -1;
			}
		}
		
		//	Fill the adjacency matrix by iterating over the list of edges
		for(int i = 0; i < getEdgeCount(); i++) {
			//	Get necessary values (source Node/target Node/weight) from next edge
			Edge	edge	= edgeList.get(i);
			int		source	= edge.getSource();
			int		target	= edge.getTarget();
			//	Write values into adjacency matrix
			connections[source][target] = i;
			connections[target][source] = i;
		}
		
		/*	Initialize Map of unvisited Nodes 
		 *	Keys are the Map IDs
		 *	Values are the distances from the initial Node
		 */
		TreeMap<Integer, Double> unvisitedNodes = new TreeMap<>();
		
		/*	Fill Map 
		 *	values are set to infinity, except for the initial node, which has value 0
		 */
		for(int i = 0; i < getNodeCount(); i++) {
			if(i == initialNodeId) {
				unvisitedNodes.put(i, (double)0);
			}else {
				unvisitedNodes.put(i, Double.POSITIVE_INFINITY);
			}
		}
		
		/*	Initialize Map of parent Nodes
		 * 	The parent Node means the Node right before on the path
		 * 	Keys are the Nodes
		 * 	Values are the parent Nodes
		 */
		TreeMap<Integer,Integer> parentNodes = new TreeMap<>();
		
		//	initialize current node
		int currentNodeId = initialNodeId;
		
		//	run trough Dijkstra's Algorithm until there are no more Nodes to check (no path possible)
		while(unvisitedNodes.get(currentNodeId) != Double.POSITIVE_INFINITY) {
			//	iterate over the adjacency matrix to find connected nodes
			for(int i = 0; i < getNodeCount(); i++) {
				//	check whether a node is connected and unvisited
				if((connections[currentNodeId][i] > -1) && unvisitedNodes.containsKey(i)) {
					//	get connecting edge
					Edge currentEdge = edgeList.get(connections[currentNodeId][i]);
					//	get weight of edge
					double weight = currentEdge.getWeight();
					// 	calculate tentative distance to neighbor node
					double tentativeDistance = unvisitedNodes.get(currentNodeId) + weight;
					//	compare current distance of neighbor node with tentative distance over current node
					if(tentativeDistance < unvisitedNodes.get(i)) {
						//	if tentative distance is smaller than current distance, replace current distance
						unvisitedNodes.put(i, tentativeDistance);
						//	add/replace entry for parent node
						parentNodes.put(i, currentNodeId);
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
		
		//	create return value
		
		//	initialize list of paths
		ArrayList<Path> paths = new ArrayList<Path>();
		
		//	create a path object for each node
		for(int destinationNodeId = 0; destinationNodeId < getNodeCount(); destinationNodeId++) {
			//	check if destination is also initial node
			if(destinationNodeId != initialNodeId) {
				//	initialize list of Nodes on the path represented by their IDs
				ArrayList<Integer> NodesOnPath = new ArrayList<Integer>();
				//	initialize list of Edges on the path represented by their IDs
				ArrayList<Integer> EdgesOnPath = new ArrayList<Integer>();
				//	get the calculated length of the path
				double length = unvisitedNodes.get(destinationNodeId);
				/*	check the length
				 * 	a value of infinity means there exists no path between the nodes
				 */
				if(length != Double.POSITIVE_INFINITY) {
					//	if the nodes have a path between them, a corresponding path object is created
					
					//	create the list of Nodes on the path
					
					/*	initialize a temporary list of the nodes
					 * 	since the parent node represents the Node before a Node on a path, the path is build backwards first
					 */
					ArrayList<Integer> NodesOnPathTemp = new ArrayList<Integer>();
					//	add the destination node as start
					NodesOnPathTemp.add(destinationNodeId);
					//	initialize the next node to check for a parent node
					int nextNode = destinationNodeId;
					//	follow parent nodes to the initial node
					while(nextNode != initialNodeId) {
						//	get parent node Id
						int parentId = parentNodes.get(nextNode);
						//	add parent node to list of nodes on path
						NodesOnPathTemp.add(parentId);
						//	set next Node to current parent Node
						nextNode = parentId;
					}
					//	fill list with Node IDs in correct order
					for(int i = NodesOnPathTemp.size() - 1; i >= 0; i--) {
						NodesOnPath.add(NodesOnPathTemp.get(i));
					}
					
					//	create list of edges on the path
					
					//	iterate over the list of nodes to get corresponding edges
					for(int i = 0; i < NodesOnPath.size() - 1; i++) {
						//	get Node IDs
						int nodeId1 = NodesOnPath.get(i);
						int nodeId2 = NodesOnPath.get(i+1);
						//	find Edge ID in adjacency matrix
						int edgeId = connections[nodeId1][nodeId2];
						//	add edge to list 
						EdgesOnPath.add(edgeId);
					}
					
		
				}else {
					/*	if there is no path between the nodes,
					 *	the list of Nodes only contains the initial and the destination Node.
					 *	The list of Edges is empty.
					 *	The length is set to infinity.
					 */
		
					//	add initial Node ID to the list
					NodesOnPath.add(initialNodeId);
					//	add destination Node ID to the list
					NodesOnPath.add(destinationNodeId);
					
				}
				//	create Path Object
				Path path = new Path(NodesOnPath, EdgesOnPath, length);
				//	add path to list of paths
				paths.add(path);
			}
		}
		//	return list of paths
		return paths;
	}
	
	/*	Method to calculate the shortest paths between all Nodes by using Dijkstra's algorithm
	 *	Returns a list of paths sorted by initial node first, destination node second
	 */
	public ArrayList<Path> shortestPaths(){
		//	initialize list of all paths
		ArrayList<Path> allPaths = new ArrayList<Path>();
		
		//	iterate over the list of nodes
		for(int i = 0; i < getNodeCount(); i++) {
			//	get list of all shortest paths with node i as initial node
			ArrayList<Path> pathsOfI = shortestPaths(i);
			//	add paths to list of all paths
			pathsOfI.forEach(path -> allPaths.add(path));
		}
		
		//	return the list of all paths
		return allPaths;
	}
	
	//	TODO remove/change (old) method implementation
	/* old implementation of getDiameter
	 * this was not usable with the new version of the shortestPath method
	 
	//Method to get the Diameter of a Graph
	public double getDiameter() { 
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
	*/
	//	Method to get the Diameter of the graph
	public double	getDiameter() {
		//	check for connectivity of the graph
		if(!isGraphConnected()) {
			/*	if the graph is not connected, the diameter is defined as infinite
			 * therefore infinity is returned
			 */
			return Double.POSITIVE_INFINITY;
		}else {
			//	get list of shortest paths
			ArrayList<Path> paths = shortestPaths();
			//	initialize longest path in list with first path in list
			Path longestPath = paths.get(0);
			//	iterate over list of paths
			for(int i = 1; i < paths.size(); i++) {
				//	compare i-th path to current longest path
				if(paths.get(i).compareTo(longestPath) > 0) {
					//	if the i-th path is longer, replace the current longest path
					longestPath = paths.get(i);
				}
			}
			//	return length of longest path
			return longestPath.getLength();
		}
	}
	
	
	// Calculate all shortestPaths and put them into a multiMap
	// return multiMap to forward them to the GraphWriter for further processing
	public MultiValuedMap<Integer, ArrayList<Integer>> getAllShortestPaths() {
		MultiValuedMap<Integer, ArrayList<Integer>> shortestPathsMap = new ArrayListValuedHashMap<>();
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
