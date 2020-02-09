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
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("EdgeID: ").append(getEdgeID());
		sb.append(" Source: ").append(getSourceNodeId());
		sb.append(" Target: ").append(getTargetNodeId());
		sb.append(" Weight: ").append(getWeight());
		return sb.toString();
	}
	
	public void printToConsole() {
		mylog.info(this.toString());
	}
}
