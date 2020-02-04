package networkHandler;

public class Edge {

	/**
	 * This class represents an Edge. An Edge contains a edgeID, a source, a target and a weight.
	 */


	/**
	 * An each is identfied via an id
	 */
	private int edgeID;

	/**
	 * Each has a source and a target node
	 */
	private int source;
	private int target;

	/**
	 * Edge has a weight
	 */
	private double weight;


	/**
	 * Default constructor
	 * @param edgeID
	 * @param source
	 * @param target
	 * @param weight
	 */

	public Edge(int edgeID, int source, int target, int weight) {
		this.edgeID = edgeID;
		this.source = source;
		this.target = target;
		this.weight = weight;
	}

	public int getEdgeID() { return edgeID; }
	public void setEdgeID(int edgeID) { this.edgeID = edgeID; }

	public int getSource() { return source; }
	public void setSource(int source) { this.source = source; }
	
	public int getTarget() { return target; }
	public void setTarget(int target) { this.target = target; }
	
	public double getWeight() { return weight; }
	public void setWeight(double weight) { this.weight = weight; }
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("EdgeID: ").append(getEdgeID());
		sb.append(" Source: ").append(getSource());
		sb.append(" Target: ").append(getTarget());
		sb.append(" Weight: ").append(getWeight());
		return sb.toString();
	}
	
	public	void	printToConsole() {
		System.out.print(this.toString() + "\n");
	}
}
