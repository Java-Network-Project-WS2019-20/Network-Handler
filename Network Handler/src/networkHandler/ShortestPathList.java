package networkHandler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;
/**
 * <p>This class is responsible to calculate shortest {@link Path}s between {@link Node}s in a given {@link Graph}.
 * <p>For this calculation multiple versions are available through this class:
 * <p>1.a) calculating all shortest {@link Path}s in the graph, allowing duplicates. The result contains two versions of a {@link Path}, one for each direction {@link Node} X to {@link Node} Y and {@link Node} Y to {@link Node} X.
 * <p>1.b) calculating all shortest {@link Path}s in the graph, excluding duplicates. The result contains only one direction of a {@link Path}, with ID of origin {@link Node} smaller than ID of destination {@link Node}.
 * <p>2. calculating only shortest {@link Path}s between two given {@link Node}s.
 * <p>3. Combining Options 1 and 2, first creating a full list of {@link Path}s and separating {@link Path}s between two given {@link Node}s afterwards. Duplicates can be allowed or excluded as well, without affecting the {@link Path}s between given {@link Node}s.
 * @author Fabian Grun
 * @see Path
 * @see GraphProperty
 * 
 */
public class ShortestPathList implements GraphProperty<TreeSet<Path>>{

	private Graph graph;
	private TreeSet<Path> shortestPathListValue;
	private TreeSet<Path> shortestPathListTwoNodes;
	private boolean calculateAll;
	private boolean calculateTwoNodes;
	private int nodeId1;
	private int nodeId2;
	private boolean correctInput;
	private boolean noDuplicates;
	private final Logger mylog = LogManager.getLogger(ShortestPathList.class);

	/**
	 * Constructor for only calculating all shortest {@link Path}s, without creating a separate list for two given {@link Node}s.
	 * @param graph An instance of {@link Graph} on which the shortest {@link Path}s are to be calculated.
	 * @param noDuplicates A boolean value indicating whether duplicate {@link Path}s need to be removed after calculation.
	 */
	public	ShortestPathList(Graph graph, boolean noDuplicates) {
		this.graph = graph;
		this.noDuplicates = noDuplicates;
		this.calculateAll = true;
		this.calculateTwoNodes = false;
		this.nodeId1 = -1;
		this.nodeId2 = -1;
	}
	
	/**
	 * Constructor for calculating shortest {@link Path}s between two given {@link Node}s. Depending on the parameters, all other shortest {@link Path}s are created as well.
	 * @param graph An instance of {@link Graph} on which the shortest {@link Path}s are to be calculated.
	 * @param noDuplicates A boolean value indicating whether duplicate {@link Path}s need to be removed after calculation.
	 * @param calculateAll A boolean value indicating whether all shortest {@link Path}s need to be calculated.
	 * @param nodeId1 ID identifying a {@link Node} from which shortest {@link Path}s need to be calculated.
	 * @param nodeId2 ID identifying a {@link Node} to which shortest {@link Path}s need to be calculated.
	 */
	public	ShortestPathList(Graph graph, boolean noDuplicates, boolean calculateAll,  int nodeId1, int nodeId2) {
		this.graph = graph;
		this.noDuplicates = noDuplicates;
		this.calculateAll = calculateAll;
		this.calculateTwoNodes = true;
		this.nodeId1 = nodeId1;
		this.nodeId2 = nodeId2;
	}
	
	/**
	 * <p>{@inheritDoc}
	 * <p>The method calls the {@link Path#printToConsole()} for each calculated shortest {@link Path}.
	 */
	public void printToConsole() {
		Iterator<Path> pathIterator = this.shortestPathListValue.iterator();
		while(pathIterator.hasNext()) {
			pathIterator.next().printToConsole();
		}
	}
	
	/**
	 * <p>Method for printing the result of the calculation for two given {@link Node}s to the console.
	 * <p>If an incorrect input was provided, an appropriate error message is returned instead.
	 */
	public void printToConsoleTwoNodes() {
		if (correctInput) {
			Iterator<Path> pathIterator = shortestPathListTwoNodes.iterator();
			while (pathIterator.hasNext()) {
				pathIterator.next().printToConsole();
			} 
		}else {
			mylog.error("The calculation for the shortest paths between the nodes n" + nodeId1 + " and n" + nodeId2 + " is not possible. Reason:" );
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
	
	/**
	 * {@inheritDoc}
	 */
	public TreeSet<Path> getValue(){
		return this.shortestPathListValue;
	}

	/**
	 * <p>{@inheritDoc}
	 * <p>The method calculates the shortest {@link Path}s following Djikstra's Algorithm. If calculating all {@link Path}s, every {@link Node} is used once as an origin for the algorithm.
	 * <p>For this a mapping of parent {@link Node}s is used to find suitable parents on the {@link Path} for each {@link Node}. From this mapping a reverse mapping is created and given to the {@link #createPaths(TreeMap, TreeMap, int, ArrayList)} method for recursive creation of the {@link Path}s.
	 * <p>Even if duplicates are to be excluded, they are calculated first and removed afterwards, because the recursive creation requires creation of all possible shortest {@link Path}s.
	 * 
	 */
	public void run() {
		mylog.debug("Started calculation of shortest paths.");
		//	initialize minimum and ceiling for initialNodeId.
		//	this is used to distinct between calculating all shortest paths or only the paths between two given nodes.
		int	initialNodeIdMin = 0;
		int	initialNodeIdCeiling = this.graph.getNodeCount();
		correctInput = true;	//used to mark given Node IDs for Two Node calculation
		if (calculateTwoNodes) {
			if (nodeId1 < 0 || nodeId1 >= graph.getNodeCount() || nodeId2 < 0 || nodeId2 >= graph.getNodeCount() || nodeId1 == nodeId2) {
				correctInput = false;
			} 
		}
		if(!calculateAll && correctInput) {	// -> only paths between two nodes are calculated
			initialNodeIdMin = nodeId1;
			initialNodeIdCeiling = nodeId1 + 1;
		}
		if (calculateAll || correctInput) {
			int[][] adjacencyMatrix = new int[this.graph.getNodeCount()][this.graph.getNodeCount()];
			/*	Fill the matrix with value -1
			 * 	This step of initialization is necessary, since the standard initialization value of 0 would be a valid entry
			 * 	-1 is therefore used to mark "empty" entries
			 */
			for (int i = 0; i < this.graph.getNodeCount(); i++) {
				for (int j = 0; j < this.graph.getNodeCount(); j++) {
					adjacencyMatrix[i][j] = -1;
				}
			}
			for (int i = 0; i < this.graph.getEdgeCount(); i++) {
				Edge edge = this.graph.getEdgeList().get(i);
				int source = edge.getSourceNodeId();
				int target = edge.getTargetNodeId();
				adjacencyMatrix[source][target] = i;
				adjacencyMatrix[target][source] = i;
			}
			this.shortestPathListValue = new TreeSet<Path>();
			for (int initialNodeId = initialNodeIdMin; initialNodeId < initialNodeIdCeiling; initialNodeId++) {
				mylog.debug("Started calculation for shortest paths originating in node n" + initialNodeId +".");
				TreeMap<Integer, Double> distancesOfUnvisitedNodesMap = new TreeMap<>();
				TreeMap<Integer, Double> distancesOfVisitedNodesMap = new TreeMap<>();
				// setting initial distances for all but the initial node to infinity to allow comparison of distances
				for (int i = 0; i < this.graph.getNodeCount(); i++) {
					if (i == initialNodeId) {
						distancesOfUnvisitedNodesMap.put(i, (double) 0);
					} else {
						distancesOfUnvisitedNodesMap.put(i, Double.POSITIVE_INFINITY);
					}
				}
				TreeMap<Integer, TreeSet<Integer>> listsOfParentNodesMap = new TreeMap<>();
				int currentNodeId = initialNodeId;
				// check if calculation for current initial node is finished:
				while (	!distancesOfUnvisitedNodesMap.isEmpty() && distancesOfUnvisitedNodesMap.get(currentNodeId) != Double.POSITIVE_INFINITY && ( calculateAll || distancesOfUnvisitedNodesMap.containsKey(nodeId2) ) ) {
					//	search for adjacent nodes
					for (int possiblyAdjacentNodeId = 0; possiblyAdjacentNodeId < this.graph.getNodeCount(); possiblyAdjacentNodeId++) {
						if ((adjacencyMatrix[currentNodeId][possiblyAdjacentNodeId] > -1) && distancesOfUnvisitedNodesMap.containsKey(possiblyAdjacentNodeId)) {
							Edge currentEdge = this.graph.getEdgeList().get(adjacencyMatrix[currentNodeId][possiblyAdjacentNodeId]);
							double weight = currentEdge.getWeight();
							double tentativeDistance = distancesOfUnvisitedNodesMap.get(currentNodeId) + weight;
							//	check for possible parent child relation of current Node and (possibly) adjacent node
							if (tentativeDistance < distancesOfUnvisitedNodesMap.get(possiblyAdjacentNodeId)) {
								distancesOfUnvisitedNodesMap.put(possiblyAdjacentNodeId, tentativeDistance);
								TreeSet<Integer> parents = new TreeSet<Integer>();
								parents.add(currentNodeId);
								listsOfParentNodesMap.put(possiblyAdjacentNodeId, parents);
							}
							else if (tentativeDistance == distancesOfUnvisitedNodesMap.get(possiblyAdjacentNodeId)) {
								TreeSet<Integer> parents = listsOfParentNodesMap.get(possiblyAdjacentNodeId);
								parents.add(currentNodeId);
							}
						}
					}
					//	move current node from unvisited to visited mapping
					distancesOfVisitedNodesMap.put(currentNodeId, distancesOfUnvisitedNodesMap.get(currentNodeId));
					distancesOfUnvisitedNodesMap.remove(currentNodeId);
					//	search for next node (if it exists)
					if (!distancesOfUnvisitedNodesMap.isEmpty()) {
						int nextNodeId = distancesOfUnvisitedNodesMap.firstKey();
						currentNodeId = nextNodeId;
						for (int i = 0; i < distancesOfUnvisitedNodesMap.size() - 1; i++) {
							nextNodeId = distancesOfUnvisitedNodesMap.higherKey(nextNodeId);
							if (distancesOfUnvisitedNodesMap.get(currentNodeId) > distancesOfUnvisitedNodesMap.get(nextNodeId)) {
								currentNodeId = nextNodeId;
							}
						}
					}
				}
				// creation of the reverse mapping for the parent-child-relationship
				TreeMap<Integer, TreeSet<Integer>> listsOfChildNodesMap = new TreeMap<>();
				Iterator<Integer> keySetIterator = listsOfParentNodesMap.keySet().iterator();
				while (keySetIterator.hasNext()) {
					int nextChildId = keySetIterator.next();
					//	check if child needs to be included in path generation
					//	if all paths are calculated always includes child
					//	if only between two paths, only visited nodes are required(allowed) for path generation. If still included, createPaths method fails to find unvisited children and fails entirely(Null Pointer Exception).
					if (calculateAll || distancesOfVisitedNodesMap.containsKey(nextChildId)) {
						listsOfParentNodesMap.get(nextChildId).forEach(parentId -> {
							if (listsOfChildNodesMap.containsKey(parentId)) {
								listsOfChildNodesMap.get(parentId).add(nextChildId);
							} else {
								TreeSet<Integer> childrenIds = new TreeSet<Integer>();
								childrenIds.add(nextChildId);
								listsOfChildNodesMap.put(parentId, childrenIds);
							}
						});
					}
				}
				ArrayList<Path> listOfAllPaths = createPaths(distancesOfVisitedNodesMap, listsOfChildNodesMap, initialNodeId, new ArrayList<Path>());
				// adding paths to nodes without connection to initial node. these paths have length infinity per definition and only contain origin and destination nodes.
				Iterator<Integer> unvisitedNodesIterator = distancesOfUnvisitedNodesMap.keySet().iterator();
				while (unvisitedNodesIterator.hasNext()) {
					int disconnectedNodeId = unvisitedNodesIterator.next();
					ArrayList<Integer> pathNodeList = new ArrayList<Integer>();
					pathNodeList.add(initialNodeId);
					pathNodeList.add(disconnectedNodeId);
					listOfAllPaths.add(new Path(pathNodeList, Double.POSITIVE_INFINITY));
				}
				
				this.shortestPathListValue.addAll(listOfAllPaths);
				mylog.debug("Finished calculation for shortest paths originating in node n" + initialNodeId +".");
			}
			mylog.debug("Finished calculation for all necessary shortest paths.");
			// creating separate list for two given nodes, only including paths between the given nodes, if necessary
			if(calculateTwoNodes && correctInput) {
				mylog.debug("started creation of separate shortest paths list for given nodes.");
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
				mylog.debug("Finished creation of separate shortest paths list for given nodes.");
			}
			
			//	remove duplicate paths if necessary
			if(noDuplicates) {
				mylog.debug("Started removing duplicate shortest paths.");
				Iterator<Path> pathIterator = shortestPathListValue.iterator();
				TreeSet<Path> temporaryList = new TreeSet<Path>();
				while(pathIterator.hasNext()) {
					Path nextPath = pathIterator.next();
					if(nextPath.getDestinationNode() > nextPath.getOriginNode()) {
						temporaryList.add(nextPath);
					}
				}
				shortestPathListValue = temporaryList;
				mylog.debug("Finished removing duplicate shortest paths.");
			}
			
		}
		

	}
	
	/**
	 * <p>This method is used to create all shortest {@link Path}s originating in a given {@link Node}.
	 * <p>On the first call it creates a single {@link Path} containing only the origin {@link Node}. By recursively calling itself the method then extends the paths by adding child nodes given by the provided mapping.
	 * <p>Once all children are added and new {@link Path}s are created or no more children are mapped the extended lists are returned and added together to form a full list of all {@link Path}s.
	 * <p>This full list is then returned to the calling method. 
	 * @param mapOfDistances
	 * @param mapOfChildIds
	 * @param currentNodeId
	 * @param pathsToCurrentNode
	 * @return ArrayList<Path>
	 * 
	 */
	/*	Method to recursively create shortest paths.
	 * 	the method takes in a map of distances of nodes from a initial node, a map of child IDs which references parent nodes with child nodes, a current node and a list of paths leading to the current node
	 * 	the maps are used to create new paths and determine whether further paths have to be created, while the list of paths leading into a node is used to determine, which paths need to be extended by the current node
	 * 	
	 */
	private ArrayList<Path> createPaths(TreeMap<Integer, Double> mapOfDistances, TreeMap<Integer, TreeSet<Integer>> mapOfChildIds, int currentNodeId, ArrayList<Path> pathsToCurrentNode) {
		ArrayList<Path> pathsEndingInCurrentNode = new ArrayList<Path>();
		ArrayList<Path> pathsContainingCurrentNode;
		// check whether the current node is the initial node for the recursive creation and create origin Path if necessary
		if(pathsToCurrentNode.isEmpty()) {
			ArrayList<Integer> nodesOnPath = new ArrayList<Integer>();
			nodesOnPath.add(currentNodeId);
			Path initialPath = new Path(nodesOnPath, 0);
			pathsEndingInCurrentNode.add(initialPath);
			// the list of paths containing the current node is initialized as an empty list, so the initial path is not added to the return value.
			pathsContainingCurrentNode = new ArrayList<Path>();
		}else{
			pathsToCurrentNode.forEach(path ->{
				Path newExtendedPath = new Path(path);
				newExtendedPath.expand(mapOfDistances.get(currentNodeId), currentNodeId);
				pathsEndingInCurrentNode.add(newExtendedPath);
			});
			// all paths ending in the current node are added to the newly created list of all path containing the node. this list will be further extended by all paths which are extensions of the just created paths.
			pathsContainingCurrentNode = new ArrayList<Path>(pathsEndingInCurrentNode);
		}
		if (mapOfChildIds.containsKey(currentNodeId)) {
			mapOfChildIds.get(currentNodeId).forEach(childId -> {
				//	the method calls itself recursively to create the paths for the children. for paths leading to the children, the list of paths ending in the current node is used. after creation, these paths are added to the overall list of paths containing the current node.
				pathsContainingCurrentNode.addAll(createPaths(mapOfDistances, mapOfChildIds, childId, pathsEndingInCurrentNode));
			});
		}
		return pathsContainingCurrentNode;
	}

}
