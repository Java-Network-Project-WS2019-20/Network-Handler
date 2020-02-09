package networkHandler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Responsible for calculation of a Minimum Spanning Tree for a {@link Graph}.
 * 
 * @author Krzysztof Goroll
 */
public class MinimumSpanningTree implements GraphProperty<ArrayList<Edge>> {

	private Graph graph;
	private ArrayList<Edge> minimumSpanningTreeValue;
	private int totalWeight;
	private Connectivity connectivity;
	private final Logger mylog = LogManager.getLogger(MinimumSpanningTree.class);

	/**
	 * Default Constructor
	 * @param graph The instance of {@link Graph} for which the {@link MinimumSpanningTree} is to be calculated.
	 * @param connectivity An instance of {@link Connectivity} indicating whether the calculation is possible.
	 */
	public MinimumSpanningTree(Graph graph, Connectivity connectivity) {
		this.graph = graph;
		this.totalWeight = 0;
		this.connectivity = connectivity;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public ArrayList<Edge> getValue() {
		return this.minimumSpanningTreeValue;
	}

	public int getTotalWeight() {
		return totalWeight;
	}

	/**
	 * This method sums the weights of all {@link Edge}s building the calculated {@link MinimumSpanningTree} and saves them in {@link MinimumSpanningTree#totalWeight}.
	 */
	private void doCalculateTotalWeight() {
		int weight = 0;
		for (Edge edge : this.minimumSpanningTreeValue) {
			weight += edge.getWeight();
		}
		totalWeight = weight;
	}

	/**
	 * <p>{@inheritDoc}
	 * <p>This Method first sorts the {@link Edge}s into a queue depending on their weights. This queue is then used to check
	 * all {@link Edge}s starting with the "lightest" for creating a tree. If an {@link Edge} can be integrated without creating a loop,
	 * it becomes part of the {@link MinimumSpanningTree}. In the end the {@link MinimumSpanningTree#doCalculateTotalWeight()} method is called
	 * to assign a total weight to the calculated tree.
	 */
	public void run() {

		if (connectivity.getValue()) {
			int edgeCount = this.graph.getEdgeCount();
			int nodeCount = this.graph.getNodeCount();
			// Create Priority Queue, sorted by weight of edges
			PriorityQueue<Edge> pq = new PriorityQueue<>(edgeCount, Comparator.comparingDouble(o -> o.getWeight()));
			for (int i = 0; i < edgeCount; i++) {
				pq.add(this.graph.getEdgeList().get(i));
			}
			// Creating parent nodes with pointer to itself
			int[] parent = new int[nodeCount];
			for (int i = 0; i < nodeCount; i++) {
				parent[i] = i;
			}
			ArrayList<Edge> mst = new ArrayList<>();
			int index = 0;
			while (index < nodeCount - 1) {
				Edge edge = pq.remove();

				int x_set = doFindParent(parent, edge.getSourceNodeId());
				int y_set = doFindParent(parent, edge.getTargetNodeId());
				// check if adding this edge creates a cycle
				if (x_set == y_set) {
					// Do nothing, will create a cycle
				} else {
					mst.add(edge);
					index++;
					doAssignNewParent(parent, x_set, y_set);
				}
			}
			this.minimumSpanningTreeValue = mst;
			doCalculateTotalWeight();
		}
	}

	/**
	 * Finds the parent of a Node Chain of parent pointers from x upwards through
	 * the tree until an element is reached whose parent is itself
	 * 
	 * @param parent current parent Nodes
	 * @param node   node to find the parents for
	 * @return parent of node
	 */
	public int doFindParent(int[] parent, int node) {
		if (parent[node] != node)
			return doFindParent(parent, parent[node]);
		return node;
	}

	/**
	 * Extend parents of a newly added edge
	 * 
	 * @param parent current parents Nodes
	 * @param x      parent of the source of appended Node
	 * @param y      parent of the target of appended Node
	 */
	public void doAssignNewParent(int[] parent, int x, int y) {
		int x_set_parent = doFindParent(parent, x);
		int y_set_parent = doFindParent(parent, y);
		// make x as parent of y
		parent[y_set_parent] = x_set_parent;
	}

	/**
	 * <p>{@inheritDoc}
	 * <p>This method prints all {@link Edge}s building the {@link MinimumSpanningTree} to the console.
	 * <p>To do this, their respective {@link Edge#printToConsole()} methods are called.
	 * <p>If the Tree cannot be calculated, an appropriate error message is returned instead.
	 */
	public void printToConsole() {

		if (connectivity.getValue()) {
			mylog.info("Minimum Spanning Tree: ");
			for (Edge e : this.minimumSpanningTreeValue) {
				e.printToConsole();
			}
		} else {
			mylog.error("Minimum Spanning Tree can not be calculated. Graph is not connected");
		}
	}

}
