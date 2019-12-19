package networkHandler;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {

//		if (args.length < 1) {
//			System.out.println("Please provide a file for processing!");
//			return;
//		}

//		String fileName = args[0];
		String fileName = "C:\\Users\\Krzysztof\\Desktop\\Uni\\Java Project\\Graphen\\medium_graph.graphml";
//		String fileName = "/home/boostr/Downloads/small_graph.graphml";
//		String fileName = "C:\\Users\\Fabian\\Downloads\\medium_graph.graphml";
//		String fileName = "C:\\Users\\khali\\OneDrive\\Desktop\\\\gra.xml";
		FileHandler nFileHandler = new FileHandler();
		nFileHandler.setGraphmlFile(fileName);

		nFileHandler.prepareParser();
		
		Graph G = new Graph(nFileHandler.getEdgeList(), nFileHandler.getNodeList());
		System.out.println(G.toString());
//		G.printAllNodes();
//		G.printAllEdges();
		System.out.println("Diameter: " + G.getDiameter());
		
		/*	Testing the shortest path algorithm
		 * 	can be removed later
		 */
		//System.out.println(G.shortestPath(0, 14));
		
	}

	
}

