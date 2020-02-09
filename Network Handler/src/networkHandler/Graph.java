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
	 * @param EdgeList an ArrayList of Edges as part of the graph
	 * @param NodeList an ArrayList of Nodes as part of the graph
	 */
	public Graph(ArrayList<Edge> EdgeList, ArrayList<Node> NodeList) {
		this.edgeList = EdgeList;
		this.nodeList = NodeList;
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

	public void printAllNodes() {
		for (Node node : nodeList) {
			node.printToConsole();
		}
	}

	public void printAllEdges() {
		for (Edge edge : edgeList) {
			edge.printToConsole();
		}
	}

	/**
	 * This method prints the
	 */
	public void printToConsole() {
		System.out.print(this.toString());
		printAllNodes();
		printAllEdges();
	}

	@Override
	/**
	 * This method creates a {@link String} representation of the Graph containing information about the numbers of Nodes and Edges.
	 * @return String
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Number of nodes: ").append(getNodeCount()).append("\n");
		sb.append("Number of egdes: ").append(getEdgeCount()).append("\n");
		return sb.toString();
	}
}
