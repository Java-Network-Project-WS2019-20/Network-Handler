package networkHandler;

import java.util.ArrayList;

/**
 * Class that represents a Graph. It stores all {@link Node}s and {@link Edge}s in respective
 * {@link ArrayList}s that are provided by the {@link GraphmlReader}.
 * 
 * @author Krzysztof Goroll
 * @author Fabian Grun
 */
public class Graph {
	
	private ArrayList<Edge> edgeList;
	private ArrayList<Node> nodeList;

	/**
	 * Default Constructor
	 * @param edgeList an ArrayList of Edges as part of the graph
	 * @param nodeList an ArrayList of Nodes as part of the graph
	 */
	public Graph(ArrayList<Edge> edgeList, ArrayList<Node> nodeList) {
		this.edgeList = edgeList;
		this.nodeList = nodeList;
	}

	public int getNodeCount() {
		return nodeList.size();
	}

	public int getEdgeCount() {
		return edgeList.size();
	}

	public ArrayList<Node> getNodeList() {
		return nodeList;
	}

	public ArrayList<Edge> getEdgeList() {
		return edgeList;
	}

	/**
	 * This method prints all information about the graph to the command line. First {@link #toString()} is called for general information
	 * and afterwards the printing methods of all {@link Node}s and {@link Edge}s in the Graph. 
	 * 
	*/
	public void printToConsole() {
		System.out.print(this.toString());
		printAllNodes();
		printAllEdges();
	}

	@Override
	/**
	 * This method creates a {@link String} representation of the Graphs information about the numbers of Nodes and Edges.
	 * @return String
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Number of nodes: ").append(getNodeCount()).append("\n");
		sb.append("Number of egdes: ").append(getEdgeCount()).append("\n");
		return sb.toString();
	}

	private void printAllNodes() {
		for (Node node : nodeList) {
			node.printToConsole();
		}
	}

	private void printAllEdges() {
		for (Edge edge : edgeList) {
			edge.printToConsole();
		}
	}

}
