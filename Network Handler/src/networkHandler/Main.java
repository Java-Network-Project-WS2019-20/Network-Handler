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
		String fileName = "D:\\Benutzer\\Desktop\\Uni\\Java Project\\Graphen\\small_graph.graphml";
//		String fileName = "/home/boostr/Downloads/small_graph.graphml";
		FileHandler nFileHandler = new FileHandler();
		nFileHandler.setGraphmlFile(fileName);

		nFileHandler.prepareParser();
		
//		nFileHandler.getNodeList().forEach((n) -> System.out.println(n));
//		nFileHandler.getEdgeList().forEach((n) -> System.out.println(n));
		
		Graph G = new Graph(nFileHandler.getEdgeList(), nFileHandler.getNodeList());
		System.out.println(G.toString());
		G.PrintAllNodes();
		G.PrintAllEdges();
	}

	
}

