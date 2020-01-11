package networkHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

public class Graph {
	private ArrayList<Edge> edgeList;
	private ArrayList<Node> nodeList;

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
		ArrayList<Integer> nodesOnPath = new ArrayList<Integer>();
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
			ArrayList<Integer> nodesOnPathTemp = new ArrayList<Integer>();
			//	add the destination node as start
			nodesOnPathTemp.add(destinationNodeId);
			//	initialize the next node to check for a parent node
			int nextNode = destinationNodeId;
			//	follow parent nodes to the initial node
			while(nextNode != initialNodeId) {
				//	get parent node Id
				int parentId = parentNodes.get(nextNode);
				//	add parent node to list of nodes on path
				nodesOnPathTemp.add(parentId);
				//	set next Node to current parent Node
				nextNode = parentId;
			}
			//	fill list with Node IDs in correct order
			for(int i = nodesOnPathTemp.size() - 1; i >= 0; i--) {
				nodesOnPath.add(nodesOnPathTemp.get(i));
			}
			

			

		}else {
			/*	if there is no path between the nodes,
			 *	the list of Nodes only contains the initial and the destination Node.
			 *	The list of Edges is empty.
			 *	The length is set to infinity.
			 */

			//	add initial Node ID to the list
			nodesOnPath.add(initialNodeId);
			//	add destination Node ID to the list
			nodesOnPath.add(destinationNodeId);
			
		}
		//	create Path Object
		Path path = new Path(nodesOnPath, length);
		//	return result
		return path;
		
	}
	



	/*	Method to calculate the shortest paths between an initial Node and all other Nodes by using Dijkstra's algorithm
	 * 	Returns a list of the paths, sorted by destination Node IDs
	 */
	public ArrayList<ArrayList<Path>> shortestPaths(int initialNodeId){

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
		 *	Keys are the Node IDs
		 *	Values are the distances from the initial Node
		 */
		TreeMap<Integer, Double> unvisitedNodesMap = new TreeMap<>();
		
		/*	Initialize Map of visited Nodes
		 * 	Keys are the Node IDs
		 * 	Values are the calculated distances from the initial Node
		 */
		TreeMap<Integer, Double> visitedNodesMap = new TreeMap<>();
		
		/*	Fill Map 
		 *	values are set to infinity, except for the initial node, which has value 0
		 */
		for(int i = 0; i < getNodeCount(); i++) {
			if(i == initialNodeId) {
				unvisitedNodesMap.put(i, (double)0);
			}else {
				unvisitedNodesMap.put(i, Double.POSITIVE_INFINITY);
			}
		}
		
		/*	Initialize Map of parent Nodes
		 * 	The parent Node means the Node right before on the path
		 * 	Keys are the Nodes
		 * 	Values are the (lists of) parent Nodes
		 */
		TreeMap<Integer,TreeSet<Integer>> parentNodesMap = new TreeMap<>();
		
		//	initialize current node
		int currentNodeId = initialNodeId;
		
		//	run trough Dijkstra's Algorithm until there are no more Nodes to check or only nodes where no path is possible
		while(!unvisitedNodesMap.isEmpty() && unvisitedNodesMap.get(currentNodeId) != Double.POSITIVE_INFINITY) {
			//	iterate over the adjacency matrix to find connected nodes
			for(int i = 0; i < getNodeCount(); i++) {
				//	check whether a node is connected and unvisited
				if((connections[currentNodeId][i] > -1) && unvisitedNodesMap.containsKey(i)) {
					//	get connecting edge
					Edge currentEdge = edgeList.get(connections[currentNodeId][i]);
					//	get weight of edge
					double weight = currentEdge.getWeight();
					// 	calculate tentative distance to neighbor node
					double tentativeDistance = unvisitedNodesMap.get(currentNodeId) + weight;
					//	compare current distance of neighbor node with tentative distance over current node
					if(tentativeDistance < unvisitedNodesMap.get(i)) {
						//	if tentative distance is smaller than current distance, replace current distance
						unvisitedNodesMap.put(i, tentativeDistance);
						//	replace entry for parent node List
						TreeSet<Integer> parents = new TreeSet<Integer>();
						parents.add(currentNodeId);
						parentNodesMap.put(i, parents);
					}
					if(tentativeDistance == unvisitedNodesMap.get(i)) {
						//	if tentative distance is equal to the current distance, another possible parent is found an added to the list of parents
						TreeSet<Integer> parents = parentNodesMap.get(i);
						parents.add(currentNodeId);
					}
				}
			}
			
			//	add current node to map of visited nodes
			visitedNodesMap.put(currentNodeId, unvisitedNodesMap.get(currentNodeId));
			//	remove current node from map of unvisited nodes
			unvisitedNodesMap.remove(currentNodeId);
			
			//	search next node, which has the smallest current distance
			
			//	ensure existence of another node to check
			if(!unvisitedNodesMap.isEmpty()) {
				//	initialize "iterator"
				int	nextNodeId = unvisitedNodesMap.firstKey();
				currentNodeId	= nextNodeId;
				
				//	iterate over the map keys
				for(int i = 0; i < unvisitedNodesMap.size() - 1; i++) {
					//	set next Node (to compare) to next higher key
					nextNodeId = unvisitedNodesMap.higherKey(nextNodeId);	
					//	compare values of next Node to current (smallest distance) node
					if(unvisitedNodesMap.get(currentNodeId) > unvisitedNodesMap.get(nextNodeId)) {
						//	replace current node by node with smaller distance
						currentNodeId = nextNodeId;
					}
				}
			}
		}

		
	/*	Initialize map of child Nodes
	 * 	While the parents represent the node before on the path, children represent the next node on the path
	 * 	This Map behaves inversely to the mapping of parents
	 * 	The parents are the keys, the lists of children are the values
	 * 	this mapping is required for easier recursive creation of the path objects
	 */
		TreeMap<Integer, TreeSet<Integer>> childNodesMap = new TreeMap<>();
		
	//	fill map of child nodes
		//	initialize iterator over set of keys(children) of map of parent nodes
		Iterator<Integer> keySetIterator = parentNodesMap.keySet().iterator();
		//	iterate over children
		while(keySetIterator.hasNext()) {
			//	set next child to check
			int nextChildId = keySetIterator.next();
			//	add entry in map of children for each parent of child
			parentNodesMap.get(nextChildId).forEach(parentId -> {
				//	check if key(parent) is already mapped
				if(childNodesMap.containsKey(parentId)) {
					//	if key(parent) is already mapped, add child to list of children
					childNodesMap.get(parentId).add(nextChildId);
				}else{
					//	if key(parent) is not mapped, initialize list of children
					TreeSet<Integer> childrenIds = new TreeSet<Integer>();
					//	add first child to list
					childrenIds.add(nextChildId);
					//	map lit of children to parent
					childNodesMap.put(parentId, childrenIds);
				}
			});
		}
		//	call createPaths method to create all paths given the parent-child relation of the nodes
		ArrayList<Path> listOfAllPaths = createPaths(visitedNodesMap, childNodesMap, initialNodeId, new ArrayList<Path>());
		
		/*	TODO: check if the following is actually necessary for the output. it does not make a difference for further calculations. diameter might depend on it but can also handle a check for connectivity instead
		 * 	add paths for all not connected nodes
		 * 	these paths have infinte length
		 */
		//	initialize iterator for the list of unvisited nodes, which have no connection to the initial node
		Iterator<Integer> unvisitedNodesIterator = unvisitedNodesMap.keySet().iterator();
		//	for each unvisited node:
		while(unvisitedNodesIterator.hasNext()) {
			//	get node ID
			int disconnectedNodeId = unvisitedNodesIterator.next();
			//	initialize list of nodes for path
			ArrayList<Integer> pathNodeList = new ArrayList<Integer>();
			//	add initial and destination node to list, so path can be identified
			pathNodeList.add(initialNodeId);
			pathNodeList.add(disconnectedNodeId);
			//	add path to list of all paths
			listOfAllPaths.add(new Path(pathNodeList, Double.POSITIVE_INFINITY));
		}
		
		/*	initialize sorted list of paths
		 * 	sorted by destination node
		 * 	each destination node is represented by a list of paths, which connect the initial node to the destination node
		 */
		ArrayList<ArrayList<Path>> sortedListOfAllPaths = new ArrayList<ArrayList<Path>>();
		//	initialize the concrete lists for the destination nodes
		for(int i = 0; i < getNodeCount(); i++) {
			sortedListOfAllPaths.add(new ArrayList<Path>());
		}
		//	sort the created paths into the corresponding lists
		for(int i = 0; i < listOfAllPaths.size(); i++) {
			sortedListOfAllPaths.get(listOfAllPaths.get(i).getDestinationNode()).add(listOfAllPaths.get(i));
		}
		
		//	return the resulting list of paths
		return sortedListOfAllPaths;

	}
	
	/*	Method to recursively create shortest paths.
	 * 	the method takes in a map of distances of nodes from a initial node, a map of child IDs which references parent nodes with child nodes, a current node and a list of paths leading to the current node
	 * 	the maps are used to create new paths and determine whether further paths have to be created, while the list of paths leading into a node is used to determine, which paths need to be extended by the current node
	 * 	
	 */
	public	ArrayList<Path>	createPaths(TreeMap<Integer, Double> mapOfDistances, TreeMap<Integer, TreeSet<Integer>> mapOfChildIds, int currentNodeId, ArrayList<Path> pathsToCurrentNode) {
		//	initialize a list of paths containing all paths, which end in the current node (of the current iteration)
		ArrayList<Path> pathsEndingInCurrentNode = new ArrayList<Path>();
		//	initialize a list of paths containing all paths, which contain the current node (of the current iteration)
		ArrayList<Path> pathsContainingCurrentNode;
		//	check whether the current node ist the initial node for the recursive creation
		if(pathsToCurrentNode.isEmpty()) {
			//	if there are no paths leading into the node, it is the initial node. therefore an initial path needs to be created, which is the basis for all paths
			//	initialize the list of nodes for the initial path
			ArrayList<Integer> nodesOnPath = new ArrayList<Integer>();
			//	add the initial node to the list of nodes
			nodesOnPath.add(currentNodeId);
			//	create the initial path(which contains only the initial node and has length 0). this path will not be contained in the list of all paths
			Path initialPath = new Path(nodesOnPath, 0);
			//	the initial path is added to the list of paths ending in the current path. this is required so it can be extended later on by further recursion.
			pathsEndingInCurrentNode.add(initialPath);
			//	 the list of paths containing the current node is initialized as an empty list, so the initial path is not added to the return value.
			pathsContainingCurrentNode = new ArrayList<Path>();
		}else{
			//	if there are paths leading into the current node, these paths need to be extended by the current node. this is done for each path.
			pathsToCurrentNode.forEach(path ->{
				//	a new path is initialized as a copy of the path leading into the current node
				Path extendedPath = new Path(path);
				//	the new path is extended by the current node and the length is updated accordingly
				extendedPath.extend(mapOfDistances.get(currentNodeId), currentNodeId);
				//	the list of paths ending in the current node is updated to contain the newly created path
				pathsEndingInCurrentNode.add(extendedPath);
			});
			//	 all paths ending in the current node are added to the newly created list of all path containing the node. this list will be further extended by all paths which are extensions of the just created paths.
			pathsContainingCurrentNode = new ArrayList<Path>(pathsEndingInCurrentNode);
		}
		
		//	the mapping of child nodes is checked for further children of the current node.
		if (mapOfChildIds.containsKey(currentNodeId)) {
			//	if the node has child nodes, their paths have to be created as well for each child with each path, that ended in the current node
			mapOfChildIds.get(currentNodeId).forEach(childId -> {
				//	the method calls itself recursively to create the paths for the children. for paths leading to the children, the list of paths ending in the current node is used. after creation, these paths are added to the overall list of paths containing the current node.
				pathsContainingCurrentNode.addAll(createPaths(mapOfDistances, mapOfChildIds, childId, pathsEndingInCurrentNode));
			});
		}
		//	the list of all paths containing the current node is returned
		return pathsContainingCurrentNode;
	}
	
	/*	Method to calculate the shortest paths between all Nodes by using Dijkstra's algorithm
	 *	Returns a list of paths sorted by initial node first, destination node second
	 */
	public ArrayList<ArrayList<ArrayList<Path>>> shortestPaths(){
		//	initialize list of all paths
		ArrayList<ArrayList<ArrayList<Path>>> allPaths = new ArrayList<ArrayList<ArrayList<Path>>>();
		
		//	iterate over the list of nodes
		for(int i = 0; i < getNodeCount(); i++) {
			//	get list of all shortest paths with node i as initial node
			allPaths.add(shortestPaths(i));
		}
		
		//	return the list of all paths
		return allPaths;
	}
	
	//	Method to get the Diameter of the graph
	public double	diameter() {
		//	check for connectivity of the graph
		if(!isGraphConnected()) {
			/*	if the graph is not connected, the diameter is defined as infinite
			 * therefore infinity is returned
			 */
			return Double.POSITIVE_INFINITY;
		}else {
			//	get list of shortest paths
			ArrayList<ArrayList<ArrayList<Path>>> paths = shortestPaths();
			//	initialize longest path in list with first path in list
			Path longestPath = paths.get(0).get(1).get(0);
			//	iterate over list of paths
			for(int i = 0; i < paths.size(); i++) {
				for(int j = i + 1; j < paths.get(i).size(); j++) {
					for(int k = 0; k < paths.get(i).get(j).size(); k++) {
						//	compare i-th path to current longest path
						if(paths.get(i).get(j).get(k).compareTo(longestPath) > 0) {
							//	if the i-th path is longer, replace the current longest path
							longestPath = paths.get(i).get(j).get(k);
						}
					}
				}
			}
			//	return length of longest path
			return longestPath.getLength();
		}
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
