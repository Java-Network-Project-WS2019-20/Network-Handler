package networkHandler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

public class ShortestPathList implements GraphProperty<TreeSet<Path>>{

	private	Graph			graph;
	private	TreeSet<Path>	shortestPathListValue;
	private	TreeSet<Path>	shortestPathListTwoNodes;
	private	boolean			calculateAll;
	private	boolean			calculateTwoNodes;
	private	int				nodeId1;
	private	int				nodeId2;
	private	boolean			correctInput;
	private	boolean			noDuplicates;
	private final Logger mylog = LogManager.getLogger(ShortestPathList.class);


	//	Constructor
	public	ShortestPathList(Graph graph, boolean noDuplicates) {
		this.graph 				= graph;
		this.noDuplicates		= noDuplicates;
		this.calculateAll		= true;
		this.calculateTwoNodes	= false;
		this.nodeId1			= -1;
		this.nodeId2			= -1;
	}
	
	public	ShortestPathList(Graph graph, boolean noDuplicates, boolean calculateAll,  int nodeId1, int nodeId2) {
		this.graph				= graph;
		this.noDuplicates		= noDuplicates;
		this.calculateAll		= calculateAll;
		this.calculateTwoNodes	= true;
		this.nodeId1			= nodeId1;
		this.nodeId2			= nodeId2;
	}
	
	public	TreeSet<Path>	getValue(){
		return this.shortestPathListValue;
	}
	
//	alternative getter method
	public	TreeSet<Path> 	getListOfShortestPathsAll(){
		return this.getValue();
	}
	
	public	TreeSet<Path>	getListOfShortestPathsTwoNodes(){
		return this.shortestPathListTwoNodes;
	}
	
//	implementation of inherited abstract method form GraphProperty class
	/*	Method to calculate the shortest paths between all Nodes by using Dijkstra's algorithm
	 *	Returns a list of paths sorted by initial node first, destination node second
	 */
	public	void	run() {
		//	initialize minimum and ceiling for initialNodeId.
		//	this is used to distinct between calculating all shortest paths or only the paths between two given nodes.
		int	initialNodeIdMin = 0;
		int	initialNodeIdCeiling = this.graph.getNodeCount();
		//	initialize boolean correctInput. This is used to check for faulty input values regarding the calculation for two given nodes and abort the calculation if necessary.
		correctInput = true;
		
		if (calculateTwoNodes) {
			//	check the input values if calculation for two nodes requested
			if (nodeId1 < 0 || nodeId1 >= graph.getNodeCount() || nodeId2 < 0 || nodeId2 >= graph.getNodeCount() || nodeId1 == nodeId2) {
				correctInput = false;
			} 
		}
		
		//	check if the calculation should be limited to the paths between two nodes
		if(!calculateAll && correctInput) {
			initialNodeIdMin = nodeId1;
			initialNodeIdCeiling = nodeId1 + 1;
		}
		
		
		
		if (calculateAll || correctInput) {
			/*	Initialize an adjacency matrix for the graph
				 * 	This matrix stores the Edge IDs of corresponding Nodes
				 * 	A positive (>= 0) entry shows an existing Edge between Nodes
				 */
			int[][] connections = new int[this.graph.getNodeCount()][this.graph.getNodeCount()];
			/*	Fill the matrix with value -1
			 * 	This step of initialization is necessary, since the standard initialization value of 0 would be a valid entry
			 * 	-1 is therefore used to mark "empty" entries
			 */
			for (int i = 0; i < this.graph.getNodeCount(); i++) {
				for (int j = 0; j < this.graph.getNodeCount(); j++) {
					connections[i][j] = -1;
				}

			}
			//	Fill the adjacency matrix by iterating over the list of edges
			for (int i = 0; i < this.graph.getEdgeCount(); i++) {
				//	Get necessary values (source Node/target Node/weight) from next edge
				Edge edge = this.graph.getEdgeList().get(i);
				int source = edge.getSource();
				int target = edge.getTarget();
				//	Write values into adjacency matrix
				connections[source][target] = i;
				connections[target][source] = i;
			}
			//	initialize list of all paths
			this.shortestPathListValue = new TreeSet<Path>();
			//	iterate over the list of nodes
			for (int initialNodeId = initialNodeIdMin; initialNodeId < initialNodeIdCeiling; initialNodeId++) {
				//	get list of all shortest paths with node i as initial node
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
				for (int i = 0; i < this.graph.getNodeCount(); i++) {
					if (i == initialNodeId) {
						unvisitedNodesMap.put(i, (double) 0);
					} else {
						unvisitedNodesMap.put(i, Double.POSITIVE_INFINITY);
					}
				}

				/*	Initialize Map of parent Nodes
				 * 	The parent Node means the Node right before on the path
				 * 	Keys are the Nodes
				 * 	Values are the (lists of) parent Nodes
				 */
				TreeMap<Integer, TreeSet<Integer>> parentNodesMap = new TreeMap<>();

				//	initialize current node
				int currentNodeId = initialNodeId;

				//	run trough Dijkstra's Algorithm until there are no more Nodes to check or only nodes where no path is possible
				while (	!unvisitedNodesMap.isEmpty()
						&& unvisitedNodesMap.get(currentNodeId) != Double.POSITIVE_INFINITY
						&& 	(calculateAll
							|| unvisitedNodesMap.containsKey(nodeId2)
							) 
						) {
					//	iterate over the adjacency matrix to find connected nodes
					for (int i = 0; i < this.graph.getNodeCount(); i++) {
						//	check whether a node is connected and unvisited
						if ((connections[currentNodeId][i] > -1) && unvisitedNodesMap.containsKey(i)) {
							//	get connecting edge
							Edge currentEdge = this.graph.getEdgeList().get(connections[currentNodeId][i]);
							//	get weight of edge
							double weight = currentEdge.getWeight();
							// 	calculate tentative distance to neighbor node
							double tentativeDistance = unvisitedNodesMap.get(currentNodeId) + weight;
							//	compare current distance of neighbor node with tentative distance over current node
							if (tentativeDistance < unvisitedNodesMap.get(i)) {
								//	if tentative distance is smaller than current distance, replace current distance
								unvisitedNodesMap.put(i, tentativeDistance);
								//	replace entry for parent node List
								TreeSet<Integer> parents = new TreeSet<Integer>();
								parents.add(currentNodeId);
								parentNodesMap.put(i, parents);
							}
							if (tentativeDistance == unvisitedNodesMap.get(i)) {
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
					if (!unvisitedNodesMap.isEmpty()) {
						//	initialize "iterator"
						int nextNodeId = unvisitedNodesMap.firstKey();
						currentNodeId = nextNodeId;

						//	iterate over the map keys
						for (int i = 0; i < unvisitedNodesMap.size() - 1; i++) {
							//	set next Node (to compare) to next higher key
							nextNodeId = unvisitedNodesMap.higherKey(nextNodeId);
							//	compare values of next Node to current (smallest distance) node
							if (unvisitedNodesMap.get(currentNodeId) > unvisitedNodesMap.get(nextNodeId)) {
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
				while (keySetIterator.hasNext()) {
					//	set next child to check
					int nextChildId = keySetIterator.next();
					//	check if child needs to be included in path generation
					//	if all paths are calculated always include child
					//	if only between two paths, only visited nodes are required for path generation .If still included, createPaths method fails to find unvisited children and fails entirely(Null Pointer Exception).
					if (calculateAll || visitedNodesMap.containsKey(nextChildId)) {
						//	add entry in map of children for each parent of child
						parentNodesMap.get(nextChildId).forEach(parentId -> {
							//	check if key(parent) is already mapped
							if (childNodesMap.containsKey(parentId)) {
								//	if key(parent) is already mapped, add child to list of children
								childNodesMap.get(parentId).add(nextChildId);
							} else {
								//	if key(parent) is not mapped, initialize list of children
								TreeSet<Integer> childrenIds = new TreeSet<Integer>();
								//	add first child to list
								childrenIds.add(nextChildId);
								//	map lit of children to parent
								childNodesMap.put(parentId, childrenIds);
							}
						});
					}
				}
				//	call createPaths method to create all paths given the parent-child relation of the nodes
				ArrayList<Path> listOfAllPaths = createPaths(visitedNodesMap, childNodesMap, initialNodeId, new ArrayList<Path>());

				/*	TODO: check if the following is actually necessary for the output. it does not make a difference for further calculations. diameter might depend on it but can also handle a check for connectivity instead
				 * 	add paths for all not connected nodes
				 * 	these paths have infinite length
				 */
				//	initialize iterator for the list of unvisited nodes, which have no connection to the initial node
				Iterator<Integer> unvisitedNodesIterator = unvisitedNodesMap.keySet().iterator();
				//	for each unvisited node:
				while (unvisitedNodesIterator.hasNext()) {
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
				
				this.shortestPathListValue.addAll(listOfAllPaths);
			}
			if(calculateTwoNodes && correctInput) {
				shortestPathListTwoNodes = new TreeSet<Path>();
				Iterator<Path> pathIterator = shortestPathListValue.iterator();
				boolean	foundAllRelevantPaths = false;
				while(pathIterator.hasNext() && !foundAllRelevantPaths) {
					Path nextPath = pathIterator.next();
					if(nextPath.getOriginNode() == nodeId1) {
						if(nextPath.getDestinationNode() == nodeId2) {
							shortestPathListTwoNodes.add(nextPath);
						}else {
							if(nextPath.getDestinationNode() > nodeId2) {
								foundAllRelevantPaths = true;
							}
						}
					}else {
						if(nextPath.getOriginNode() > nodeId1) {
							foundAllRelevantPaths = true;
						}
					}
				}
			}
			
			//	remove duplicate paths if necessary
			if(noDuplicates) {
				Iterator<Path> pathIterator = shortestPathListValue.iterator();
				TreeSet<Path> temporaryList = new TreeSet<Path>();
				while(pathIterator.hasNext()) {
					Path nextPath = pathIterator.next();
					if(nextPath.getDestinationNode() > nextPath.getOriginNode()) {
						temporaryList.add(nextPath);
					}
				}
				shortestPathListValue = temporaryList;
			}
			
		}
		

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
		//	check whether the current node is the initial node for the recursive creation
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

	public	void	printToConsole() {
		Iterator<Path> pathIterator = this.shortestPathListValue.iterator();
		while(pathIterator.hasNext()) {
			pathIterator.next().printToConsole();
		}
	}
	
	public	void	printToConsoleTwoNodes() {
		
		if (correctInput) {
			Iterator<Path> pathIterator = shortestPathListTwoNodes.iterator();
			while (pathIterator.hasNext()) {
				pathIterator.next().printToConsole();
			} 
		}else {

			mylog.error("The calculation for the shortest paths between the nodes n" + nodeId1 + " and n" + nodeId2 + " is not possible. Reason:" );
//			System.out.print("The calculation for the shortest paths between the nodes n" + nodeId1 + " and n" + nodeId2 + " is not possible.\nReason: ");
			if(nodeId1 == nodeId2) {

				mylog.error("The given Node IDs can not be equal.\n");
			}else {
				boolean node1IsFaulty	= false;
				boolean node2IsFaulty	= false;
				if(nodeId1 < 0 || nodeId1 >= graph.getNodeCount()) {
					node1IsFaulty	= true;
				}
				if(nodeId2 < 0 || nodeId2 >= graph.getNodeCount()) {
					node2IsFaulty	= true;
				}
				if(node1IsFaulty && node2IsFaulty) {
					mylog.error("Both nodes do not exist in given graph.\n");
				}else {
					if(node1IsFaulty) {
						mylog.error("Node n" + nodeId1 + " does not exist in given graph.\n");
					}
					if(node2IsFaulty) {
						mylog.error("Node n" + nodeId2 + " does not exist in given graph.\n");
					}
				}
			}
		}
	}
}
