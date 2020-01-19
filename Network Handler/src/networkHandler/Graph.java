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
	
	// Constructor
	Graph (ArrayList<Edge> EdgeList, ArrayList<Node> NodeList){
		this.edgeList = EdgeList;
		this.nodeList = NodeList;
	}
	
	
	// getter - setter
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
	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Number of nodes: ").append(getNodeCount()).append("\n");
		sb.append("Number of egdes: ").append(getEdgeCount()).append("\n");
		return sb.toString();
	}
	
}
