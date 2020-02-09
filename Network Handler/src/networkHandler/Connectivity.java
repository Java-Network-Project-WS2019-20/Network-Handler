package networkHandler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.LinkedList;

/**
 * Calculates whether a {@link Graph} is connected or not. Using the Depth First
 * Search Algorithm
 * 
 * @author Krzysztof
 */

public class Connectivity implements GraphProperty<Boolean> {

	private Graph graph;
	private Boolean connectivityValue;
	private final Logger mylog = LogManager.getLogger(Connectivity.class);

	public Connectivity(Graph graph) {
		this.graph = graph;
	}

	public Boolean getValue() {
		return this.connectivityValue;
	}

	@SuppressWarnings("unchecked")
	public void run() {

		// A array of linked lists of all adjacent Nodes for each Node
		// Size of array is the number of Nodes
		LinkedList<Integer>[] adjacentListArray = new LinkedList[this.graph.getNodeCount()];
		for (int nodeId = 0; nodeId < this.graph.getNodeCount(); nodeId++) {
			adjacentListArray[nodeId] = new LinkedList<Integer>();
		}
		// Adds all edges to the nodes
		// Since graph is undirected, add an edge from source to target as well as
		// target to source
		for (Edge edge : this.graph.getEdgeList()) {
			adjacentListArray[edge.getSource()].add(edge.getTarget());
			adjacentListArray[edge.getTarget()].add(edge.getSource());
		}
		boolean[] visited = new boolean[this.graph.getNodeCount()];
		// Calculate all reachable Nodes
		// If one Node is not reachable it means the graph is not connected
		doVisitNodes(0, visited, adjacentListArray);
		this.connectivityValue = true;
		for (int nodeId = 0; nodeId < this.graph.getNodeCount(); nodeId++) {
			if (!visited[nodeId])
				this.connectivityValue = false;
		}
	}

	/**
	 * Method to calculate, which nodes are reachable from a certain node v
	 * 
	 * @param startNodeId
	 * @param visitedNodes
	 * @param adjacentListArray A array of linked lists of all adjacent Nodes for
	 *                          each Node
	 * @return boolean[] of visited nodes afterwards
	 */
	public boolean[] doVisitNodes(int startNodeId, boolean[] visitedNodes, LinkedList<Integer>[] adjacentListArray) {
		visitedNodes[startNodeId] = true;
		// repeat for all the nodes adjacent to this node
		for (int adjacentNodeId : adjacentListArray[startNodeId]) {
			if (!visitedNodes[adjacentNodeId])
				doVisitNodes(adjacentNodeId, visitedNodes, adjacentListArray);
		}
		return visitedNodes;
	}

	/**
	 * {@inheritDoc}
	 */
	public void printToConsole() {
		String outputString = "The graph is";
		if (!getValue()) {
		outputString = outputString + "not";
		}
		outputString = outputString + "connected";
		mylog.info(outputString);
	}
}
