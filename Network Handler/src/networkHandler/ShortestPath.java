package networkHandler;

import java.util.ArrayList;
import java.util.TreeMap;

public class ShortestPath implements GraphProperty<Path>{
//	Attributes
	private	Graph	graph;
	private	Path	shortestPathValue;
	private	int		initialNodeId;
	private int		destinationNodeId;
	
//	Constructor
	public	ShortestPath(Graph graph, int initialNodeId, int destinationNodeId) {
		this.graph				= graph;
		this.initialNodeId 		= initialNodeId;
		this.destinationNodeId	= destinationNodeId;
	}
	
	public	Path	getValue() {
		return this.shortestPathValue;
	}
	
//	alterantive getter method
	public	Path	getShortestPath() {
		return this.getValue();
	}

//	implementation of calculate method from GraphProperty Superclass
	//	Method to calculate the shortest path between two given Nodes by using Dijkstra's algorithm.
	public	void	run() {
		
			/*	Initialize an adjacency matrix for the graph
			 * 	This matrix stores the Edge IDs of corresponding Nodes
			 * 	A positive (>= 0) entry shows an existing Edge between Nodes
			 */
			int[][] connections = new int[this.graph.getNodeCount()][this.graph.getNodeCount()];
			
			/*	Fill the matrix with value -1
			 * 	This step of initialization is necessary, since the standard initialization value of 0 would be a valid entry
			 * 	-1 is therefore used to mark "empty" entries
			 */
			for(int i = 0; i < this.graph.getNodeCount(); i++) {
				for(int j = 0; j < this.graph.getNodeCount(); j++) {
					connections[i][j] = -1;
				}
			}

			//	Fill the adjacency matrix by iterating over the list of edges
			for(int i = 0; i < this.graph.getEdgeCount(); i++) {
				//	Get necessary values (source Node/target Node/weight) from next edge
				Edge	edge	= this.graph.getEdgeList().get(i);
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
			for(int i = 0; i < this.graph.getNodeCount(); i++) {
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
				for(int i = 0; i < this.graph.getNodeCount(); i++) {
					//	check whether a node is connected and unvisited
					if((connections[currentNodeId][i] > -1) && unvisitedNodes.containsKey(i)) {
						//	get connecting edge
						Edge currentEdge = this.graph.getEdgeList().get(connections[currentNodeId][i]);
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
			this.shortestPathValue = new Path(nodesOnPath, length);
			
			
		
		

	}
	
	public	void	printToConsole() {
		
	}
}
