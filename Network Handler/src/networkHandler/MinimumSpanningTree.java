package networkHandler;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

public class MinimumSpanningTree implements GraphProperty<ArrayList<Edge>>{
	
	private	Graph			graph;
	private	ArrayList<Edge>	minimumSpanningTreeValue;
	private int 			mstWeight; 
	
	public 					MinimumSpanningTree(Graph graph) {
		this.graph		= graph;
		this.mstWeight	= 0;
	}
	
	public	ArrayList<Edge>	getValue() {
		return this.minimumSpanningTreeValue;
	}
	
	public	int 			getMstWeight () {
		return mstWeight;
	}
	
	public	void			calculateMstWeight() {
		int weight = 0;
		for (Edge e : this.minimumSpanningTreeValue) {
			weight += e.getWeight();
		}
		mstWeight = weight;
	}
	
	/**
	 * Finds the parent of a Node
	 * @param parent current parent Nodes
	 * @param node to find the parents for
	 * @return parent of node
	 */
	public	int				find(int [] parent, int node) {
		if(parent[node] != node)
			return find(parent, parent[node]);
		return node;
	}
	
	/**
	 * Extend parents of newly added edge
	 * @param parent current parents Nodes
	 * @param x parent of the source of appended Node
	 * @param y parent of the target of appended Node
	 */
	public	void			extend(int [] parent, int x, int y) {
		int x_set_parent = find(parent, x);
		int y_set_parent = find(parent, y);
		//make x as parent of y
		parent[y_set_parent] = x_set_parent;
	}
	
	/**
	 * Print the Minimum Spanning Tree
	 * 
	 */
	public	void			printToConsole(){
		System.out.println("Minimum Spanning Tree: ");
		for (Edge e : this.minimumSpanningTreeValue) {
			StringBuilder sb = new StringBuilder();
			sb.append("Source: ").append(e.getSource()).append(" Target: ").
			append(e.getTarget()).append(" Weight: ").append(e.getWeight()).append("    Edge ID: ").append(e.getEdgeID());
			System.out.println(sb);
		}
	}
	
	public	void			run() {
		
		int edgeCount = this.graph.getEdgeCount();
		int nodeCount = this.graph.getNodeCount();
		
		//Create Priority Queue, sorted by weight of edges
		PriorityQueue<Edge> pq = new PriorityQueue<>(edgeCount,Comparator.comparingDouble(o -> o.getWeight()));
		
		for (int i = 0; i < edgeCount; i++) {
			pq.add(this.graph.getEdgeList().get(i));
		}
		
		//Creating parent nodes with pointer to itself
		int[] parent = new int[nodeCount];
		for(int i = 0; i < nodeCount; i++) {
				parent[i] = i;
			}
			
		ArrayList<Edge> mst = new ArrayList <>();
		
		int index = 0;
		while(index < nodeCount - 1) {
			Edge edge = pq.remove();
			
			int x_set = find(parent, edge.getSource());
			int y_set = find(parent, edge.getTarget());
			//check if adding this edge creates a cycle
			if(x_set == y_set) {
				//Do nothing
			} else {
				mst.add(edge);
				index++;
				extend(parent, x_set, y_set);
			}
		}

		this.minimumSpanningTreeValue = mst;
		calculateMstWeight();
	}	

}
