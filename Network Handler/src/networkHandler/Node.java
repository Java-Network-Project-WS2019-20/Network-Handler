package networkHandler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

	/**
 	* This class represents a node in a {@link Graph}.
 	* @author Sebastian Monok
 	*/

public class Node {
	private int id;
	private final Logger mylog = LogManager.getLogger(Node.class);

	/**
	 * Default Constructor
	 * @param id ID identifying the {@link Node}
	 */
	public Node(int id) {
		this.id = id;
	}

	public int getID() { return id; }

	@Override
	/**
	 * This method creates a String representation of the information about the {@link Node}, naimly its ID.
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("NodeID: ").append(getID());
		return sb.toString();
	}
	
	/**
	 * This method prints the information returned by {@link Node#toString()} to the console.
	 */
	public void printToConsole() {
		mylog.info(this.toString());
	}
}

