package communicationNetworkAnalysis;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
	
		GraphImporter gi = new GraphImporter("/home/boostr/Downloads/small_graph.graphml");
		
		System.out.println();
		gi.getGraph().getNodeMap().forEach((key, value) -> System.out.println(key + " = " + value));
		System.out.println("# of Nodes: " + gi.getGraph().getNodeMap().size());
		System.out.println();
		
		gi.getGraph().getEdgeMap().forEach((key, value) -> System.out.println(key + " = " + value));
		System.out.println("# of Edges: " + gi.getGraph().getEdgeMap().size());
		System.out.println();
	
	}

	
}

