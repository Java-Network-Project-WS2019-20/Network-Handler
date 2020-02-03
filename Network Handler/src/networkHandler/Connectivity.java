package networkHandler;

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

	public Connectivity(Graph graph) {
		this.graph = graph;
	}

	public Boolean getValue() {
		return this.connectivityValue;
	}

	public Boolean getConnectivity() {
		return this.getValue();
	}

	@SuppressWarnings("unchecked")
	public void run() {

		// A array of linked lists of all adjacent Nodes for each Node
		// Size of array is the number of Nodes
		LinkedList<Integer>[] adjacentListArray = new LinkedList[this.graph.getNodeCount()];
		for (int i = 0; i < this.graph.getNodeCount(); i++) {
			adjacentListArray[i] = new LinkedList<Integer>();
		}
		// Adds all edges to the nodes
		// Since graph is undirected, add an edge from source to target as well as
		// target to source
		for (Edge e : this.graph.getEdgeList()) {
			adjacentListArray[e.getSource()].add(e.getTarget());
			adjacentListArray[e.getTarget()].add(e.getSource());
		}
		boolean[] visited = new boolean[this.graph.getNodeCount()];
		// Calculate all reachable Nodes
		// If one Node is not reachable it means the graph is not connected
		doVisitNodes(0, visited, adjacentListArray);
		this.connectivityValue = true;
		for (int v = 0; v < this.graph.getNodeCount(); v++) {
			if (!visited[v])
				this.connectivityValue = false;
		}
	}

	/**
	 * Method to calculate, which nodes are reachable from a certain node v
	 * 
	 * @param v                 start node
	 * @param visited           all visited nodes
	 * @param adjacentListArray A array of linked lists of all adjacent Nodes for
	 *                          each Node
	 * @return visited nodes afterwards
	 */
	public boolean[] doVisitNodes(int v, boolean[] visited, LinkedList<Integer>[] adjacentListArray) {
		visited[v] = true;
		// repeat for all the nodes adjacent to this node
		for (int x : adjacentListArray[v]) {
			if (!visited[x])
				doVisitNodes(x, visited, adjacentListArray);
		}
		return visited;
	}

	public void printToConsole() {
		System.out.print("The graph is ");
		if (!getValue()) {
			System.out.print("not ");
		}
		System.out.print("connected.\n");
	}
}
