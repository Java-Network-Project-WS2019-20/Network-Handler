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
	
	/**
	 * Default Constructor given a list of {@link Node}s and a value for the {@link Path#length}.
	 * @param nodes {@link ArrayList} of {@link Node}s sorted by their order on the {@link Path}.
	 * @param length double value indicating the length of the {@link Path}.
	 */
	public Path(ArrayList<Integer> nodes, double length){
		this.nodes = new ArrayList<Integer>();
		this.nodes.addAll(nodes);
		this.length = length;
	}

	/**
	 * Copy Constructor, which creates a copy of the given {@link Path}.
	 * @param that An instance of {@link Path} which is to be copied.
	 */
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

	/**
	 * <p>The method is used to expand a {@link Path} with another {@link Node}.
	 * <p>The given {@link Node} is appended to the {@link ArrayList} of {@link Node}s, while the length is set to the new given value.
	 * @param length New length of the {@link Path}
	 * @param NodeId ID identifying the {@link Node} to append to the {@link Path}
	 */
	public void expand(double length, int NodeId) {
		this.setLength(length);
		this.nodes.add(NodeId);
	}

	/**
	 * This method check whether the {@link Path} contains a given {@link Node}.
	 * @param NodeId ID identifying the {@link Node} which is to be checked.
	 * @return boolean (true if {@link Node} lies on {@link Path}, otherwise false
	 */
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

	/**
	 * This method is used to check two {@link Path}s for equality
	 * @param that another {@link Path} instance to be compared with the {@link Path}
	 * @return boolean, true if the {@link Path}s are equal, otherwise false
	 */
	public boolean equals(Path that) {
		return (this.length == that.length  && this.getNumberOfNodes() == that.getNumberOfNodes() && this.nodes.containsAll(that.nodes));
	}
	
	/**
	 * This method prints the information of the {@link Path} as a {@link String} to the Console. The Information consists of the origin {@link Node}, the destination {@link Node},
	 * the other {@link Node}s on the {@link Path} in order and the length.
	 */
	public void printToConsole() {
		StringBuilder stringBuilder = new StringBuilder("Shortest path between n" + getOriginNode() + " and n" + getDestinationNode() + ": {");
		for(int i = 0; i < getNumberOfNodes() - 1; i++) {
			stringBuilder.append("n" + getNode(i) + ",");
		}
		stringBuilder.append("n" + getDestinationNode() + "}, length: " + getLength() + "\n");
		mylog.info(stringBuilder.toString());
	}
}
