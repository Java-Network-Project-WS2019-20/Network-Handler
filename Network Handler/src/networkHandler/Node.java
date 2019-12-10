package networkHandler;

public class Node {
	private int id;


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

}

