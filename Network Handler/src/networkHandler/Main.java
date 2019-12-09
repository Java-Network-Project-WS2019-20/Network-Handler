package networkHandler;

public class Main {

	public static void main(String[] args) {	
		
		// put as parameter path of the desired graphml file
		GraphmlHandler gh = new GraphmlHandler("/home/boostr/Downloads/small_graph.graphml");
		
		gh.getNodeList().forEach(value -> System.out.println(value));
		System.out.println("# of Nodes: " + gh.getNodeList().size());
		
		System.out.println();
		
		gh.getEdgeList().forEach(value -> System.out.println(value));
		System.out.println("# of Edges: " + gh.getEdgeList().size());

	
	}

	
}

