package networkHandler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Calculates a Minimum Spanning Tree for a {@link Graph}
 * 
 * @author Krzysztof
 */
public class MinimumSpanningTree implements GraphProperty<ArrayList<Edge>> {

	private Graph graph;
	private ArrayList<Edge> minimumSpanningTreeValue;
	private int mstWeight;
	private Connectivity connectivity;

	public MinimumSpanningTree(Graph graph, Connectivity connectivity) {
		this.graph = graph;
		this.mstWeight = 0;
		this.connectivity = connectivity;
	}

	public ArrayList<Edge> getValue() {
		return this.minimumSpanningTreeValue;
	}

	public int getMstWeight() {
		return mstWeight;
	}

	public void doCalculateMstWeight() {
		int weight = 0;
		for (Edge e : this.minimumSpanningTreeValue) {
			weight += e.getWeight();
		}
		mstWeight = weight;
	}

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

				int x_set = doFindParent(parent, edge.getSource());
				int y_set = doFindParent(parent, edge.getTarget());
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
			doCalculateMstWeight();
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

	public void printToConsole() {

		if (connectivity.getValue()) {
			System.out.println("Minimum Spanning Tree: ");
			for (Edge e : this.minimumSpanningTreeValue) {
				e.printToConsole();
			}
		} else {
			System.out.print("Minimum Spanning Tree can not be calculated. Graph is not connected.\n");
		}
	}

}
