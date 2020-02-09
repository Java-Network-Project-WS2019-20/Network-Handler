package networkHandler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

	/**
 	* This class represents a node in a {@link Graph}.
 	*/

public class Node {
	private int id;
	private final Logger mylog = LogManager.getLogger(Node.class);

	public Node(int id) {
		this.id = id;
	}

	public int getID() { return id; }

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("NodeID: ").append(getID());
		return sb.toString();
	}
	
	public void printToConsole() {
		mylog.info(this.toString());
	}
}

