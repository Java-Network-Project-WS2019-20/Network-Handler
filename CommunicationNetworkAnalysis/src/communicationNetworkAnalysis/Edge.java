package communicationNetworkAnalysis;

public class Edge {
	private int edge_ID;
	private int source;
	private int target;
	private int weight;
	
	
	public Edge (int id, int s, int t, int w) {
		setEdge_ID(id);
		setSource(s);
		setTarget(t);
		setWeight(w);
	}
	
	
	public int getEdge_ID() { return edge_ID; }
	public void setEdge_ID(int edge_ID) { this.edge_ID = edge_ID; }

	public int getSource() { return source; }
	public void setSource(int source) { this.source = source; }
	
	public int getTarget() { return target; }
	public void setTarget(int target) { this.target = target; }
	
	public int getWeight() { return weight; }
	public void setWeight(int weight) { this.weight = weight; }
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("EdgeID: ").append(getEdge_ID());
		sb.append(" Source: ").append(getSource());
		sb.append(" Target: ").append(getTarget());
		sb.append(" Weight: ").append(getWeight());
		return sb.toString();
	}
	
}

