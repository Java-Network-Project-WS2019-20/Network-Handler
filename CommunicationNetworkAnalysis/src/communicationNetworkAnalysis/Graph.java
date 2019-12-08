package communicationNetworkAnalysis;

import java.util.HashMap;

public class Graph {

	private HashMap<Integer, Node> nodeMap = new HashMap<Integer, Node>();
	private HashMap<Integer, Edge> edgeMap = new HashMap<Integer, Edge>();
	
	
	//TODO is there a need for a constructor? standard constructor is created by the jvm anyways
	
	
	public HashMap<Integer, Node> getNodeMap() { return nodeMap; }
	public void setNodeMap(HashMap<Integer, Node> nodeMap) { this.nodeMap = nodeMap; }
	public HashMap<Integer, Edge> getEdgeMap() { return edgeMap; }
	public void setEdgeMap(HashMap<Integer, Edge> edgeMap) { this.edgeMap = edgeMap; }
			
	
	public void addNode(int id, Node n) {
		nodeMap.put(id, n);
	}
	
	public void addEdge(int id, Edge e) {
		edgeMap.put(id, e);
	}


}

