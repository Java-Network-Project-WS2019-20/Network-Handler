package communicationNetworkAnalysis;

public class Node {
	private int node_ID;
	
	
	public Node(int node_ID) {
		setNode_id(node_ID);
	}
	
	
	public int getNode_id() { return node_ID; }
	public void setNode_id(int node_ID) { this.node_ID = node_ID; }
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("NodeID: ").append(getNode_id());
		return sb.toString();
	}

}

