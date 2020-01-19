package networkHandler;

import java.util.LinkedList;

public class Connectivity extends GraphProperty<Boolean>{
//	Constructor
	public	Connectivity(Graph graph) {
		super(graph);
	}
	
//	alternative getter method
	public boolean	getConnectivity() {
		return this.getValue();
	}
	
//	implementation of calculate method from GraphProperty Superclass
	// Method to calculate, whether a undirected Graph is connected
	// Not minding the weight of the edges
	public void calculate() {

		// A linked list of all adjacent Nodes for each Node
		// Size of array is the number of Nodes
		LinkedList<Integer>[] adjListArray = new LinkedList[this.graph.getNodeCount()];
		
		// Create a new list for each Node
		// so that adjacent nodes can be stored
		for (int i = 0; i < this.graph.getNodeCount(); i++) {
		adjListArray[i] = new LinkedList<Integer>();
		}
		// Adds all edges to the nodes
		// Since graph is undirected, add an edge from source to target
		// as well as target to source
		for (Edge e : this.graph.getEdgeList()) {
			adjListArray[e.getSource()].add(e.getTarget());
			adjListArray[e.getTarget()].add(e.getSource());
		}
		
		// Mark all Nodes as not visited
		boolean[] visited = new boolean[this.graph.getNodeCount()];
		// Calculate all reachable Nodes from Node 1
		// If one Node is not reachable, it means the graph
		// Is not connected
		dfsVisit(0,visited,adjListArray);
		for(int v = 0; v < this.graph.getNodeCount(); v++) {
			if(!visited[v])
				this.value = false;
		}
		this.value = true;
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

}