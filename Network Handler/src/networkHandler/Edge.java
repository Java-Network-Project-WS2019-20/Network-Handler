package networkHandler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
/**
 * This class represents an Edge between two {@link Node}s of a {@link Graph}.
 */
public class Edge {
	
	private int edgeID;
	private int sourceNodeId;
	private int targetNodeId;
	private double weight;
	private final Logger mylog = LogManager.getLogger(Edge.class);

	/**
	 * Default Constructor
	 * @param edgeID
	 * @param sourceNodeId
	 * @param targetNodeId
	 * @param weight
	 */
	public Edge(int edgeID, int sourceNodeId, int targetNodeId, int weight) {
		this.edgeID = edgeID;
		this.sourceNodeId = sourceNodeId;
		this.targetNodeId = targetNodeId;
		this.weight = weight;
	}

	public int getEdgeID() {
		return edgeID;
	}

	public int getSourceNodeId() {
		return sourceNodeId;
	}
	
	public int getTargetNodeId() {
		return targetNodeId;
	}
	
	public double getWeight() {
		return weight;
	}

	@Override
	/**
	 * This method created a {@link String} presentation of the edge containing all informations.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("EdgeID: ").append(getEdgeID());
		sb.append(" Source: ").append(getSourceNodeId());
		sb.append(" Target: ").append(getTargetNodeId());
		sb.append(" Weight: ").append(getWeight());
		return sb.toString();
	}
	
	/**
	 * This method prints the {@link String} presentation returned by {@link #toString()} to the console.
	 */
	public void printToConsole() {
		mylog.info(this.toString());
	}
}
