package networkHandler;

public class Node {


	/**
	 * This class represents a node
	 */

	/**
	 * A node is identified with an id
	 */
	private int id;


	/**
	 * Default Constructor
	 * @param id
	 */

	public Node(int id) {
		this.id = id;
	}

	public int getID() { return id; }
	public void setID(int id) { this.id = id; }
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("NodeID: ").append(getID());
		return sb.toString();
	}
	
	public	void	printToConsole() {
		System.out.print(this.toString()+ "\n");
	}
}

