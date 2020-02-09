package networkHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
/**
 * <p>This class represents a path of {@link Node}s in a {@link Graph}.
 * <p>It consists of an {@link ArrayList} of {@link Node}s sorted by their order of occurrence on the path and a length.
 * <p>Paths are also expandable by adding further {@link Node}s and adding to their length.
 * <p>This class also implements the {@link Comparable} interface for easier recursive construction by the {@link ShortestPathList} class and sorting of paths.
 * <p>Note: this class has a natural ordering that is inconsistent with equals.
 * @author Fabian Grun
 *
 */
public class Path implements Comparable<Path>{
	
	private ArrayList<Integer> nodes;
	private double length;
	private final Logger mylog = LogManager.getLogger(Path.class);
	
	public Path(ArrayList<Integer> nodes, double length){
		this.nodes = new ArrayList<Integer>();
		this.nodes.addAll(nodes);
		this.length = length;
	}

	public Path(Path that) {
		this.nodes = new ArrayList<Integer>();
		this.nodes.addAll(that.nodes);
		this.length = that.length;
	}

	public int getOriginNode() {
		return this.nodes.get(0);
	}

	public int getDestinationNode() {
		return this.nodes.get(this.nodes.size() - 1);
	}

	public int getNumberOfNodes() {
		return this.nodes.size();
	}

	public int getNode(int i) {
		return this.nodes.get(i);
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public void expand(double length, int NodeId) {
		this.setLength(length);
		this.nodes.add(NodeId);
	}

	public boolean contains(int NodeId) {
		return this.nodes.contains(NodeId);
		
	}
	
	/**
	 * <p>Implementation of the compareTo method inherited from the {@link Comparable} interface.
	 * <p>Paths are sorted by their Origin {@link Node} (Index 0 in list) first, their destination {@link Node} second.
	 * <p>If both origin and destination {@link Node} are equal, the number of {@link Node}s on the path is used next ("shorter" paths before "longer") and if equal as well, the remaining {@link Node}s on the path are compared.
	 */
	public int compareTo(Path that) {
		if(this.getOriginNode() == that.getOriginNode() && this.getDestinationNode() == that.getDestinationNode()) {
			if(this.getNumberOfNodes() < that.getNumberOfNodes()) {
				return -1;
			}else if (this.getNumberOfNodes() > that.getNumberOfNodes()){
				return 1;
			}else {
				for(int i = 0; i < this.getNumberOfNodes(); i++) {
					if(this.getNode(i) < that.getNode(i)) {
						return -1;
					}else if(this.getNode(i) > that.getNode(i)){
						return 1;
					}
				}
			}
			return 0;
		}else {
			if(this.getOriginNode() == that.getOriginNode()) {
				if(this.getDestinationNode() < that.getDestinationNode()) {
					return -1;
				}else {
					return 1;
				}
			}else{
				if(this.getOriginNode() < that.getOriginNode()) {
					return -1;
				}else {
					return 1;
				}
			}
		}
	}

	public boolean equals(Path that) {
		return (this.length == that.length  && this.getNumberOfNodes() == that.getNumberOfNodes() && this.nodes.containsAll(that.nodes));
	}
	
	public void printToConsole() {
		StringBuilder stringBuilder = new StringBuilder("Shortest path between n" + getOriginNode() + " and n" + getDestinationNode() + ": {");
		for(int i = 0; i < getNumberOfNodes() - 1; i++) {
			stringBuilder.append("n" + getNode(i) + ",");
		}
		stringBuilder.append("n" + getDestinationNode() + "}, length: " + getLength() + "\n");
		mylog.info(stringBuilder.toString());
	}
}
