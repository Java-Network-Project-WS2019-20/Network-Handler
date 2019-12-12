package networkHandler;
import java.util.ArrayList;
import java.util.LinkedList;

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
			adjListArray[e.getSource()-1].add(e.getTarget()-1);
			adjListArray[e.getTarget()-1].add(e.getSource()-1);
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
	
}
