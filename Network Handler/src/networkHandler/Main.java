package networkHandler;

import org.apache.commons.cli.ParseException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.NoSuchElementException;

public class Main {

	public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

//<<<<<<< HEAD
//		String fileName = "C:\\Users\\Krzysztof\\Desktop\\Uni\\Java Project\\Graphen\\medium_graph.graphml";
//		String fileName = "C:\\Users\\Fabian\\Downloads\\medium_graph.graphml";
//		String fileName = "C:\\Users\\khali\\OneDrive\\Desktop\\\\gra.xml";
//		FileHandler nFileHandler = new FileHandler();
//		nFileHandler.setGraphmlFile(fileName);
//		
//		nFileHandler.prepareParser();
//		
//		Graph G = new Graph(nFileHandler.getEdgeList(), nFileHandler.getNodeList());
//		System.out.println(G.toString());
//		G.printAllNodes();
//		G.printAllEdges();
//		System.out.println("Diameter: " + G.getDiameter());
		
		/*	Testing the shortest path algorithm
		 * 	can be removed later
		 */
		//System.out.println(G.shortestPath(0, 14));
//=======
		
		// cla (command line arguments) passed for testing:
		// C:\\Users\\boost\\Downloads\\small_graph.graphml -a /outputfile.graphml -b 2 -c -d -s 1 14
		try {
			if (args.length > 0) {
					// start parsing cla
				CommandLineHandler clHandler = new CommandLineHandler(args);
				clHandler.claParser();

					// start parsing the graph
				FileHandler nFileHandler = new FileHandler();

				if(clHandler.getInputFileName() != null) {
					nFileHandler.setGraphmlFile(clHandler.getInputFileName());
					nFileHandler.prepareParser();
				}
				
				Graph G = new Graph(nFileHandler.getEdgeList(), nFileHandler.getNodeList());
//				System.out.println(G.toString());
//				G.printAllNodes();
//				G.printAllEdges();
				
				if(clHandler.getConnectivity() == true) {
					//System.out for testing only
					System.out.println("Graph is connected: " + G.isGraphConnected());
				}
				
				
				try {
					if(clHandler.getShortestPath() == true) {
						//System.out for testing only
						System.out.println( "Shortest Path between: "
								+ clHandler.getShortestPathNodeIDs()[0] + " and "
								+ clHandler.getShortestPathNodeIDs()[1] + " = "
								+ G.shortestPath(clHandler.getShortestPathNodeIDs()[0], clHandler.getShortestPathNodeIDs()[1]));
					}
				} catch (NoSuchElementException nsee) {
					System.out.println("ERROR: Can not calculate shortest path."
							+ "\n	The Node ID you provided does not exist."
							+ "\n	use -G or --graph to display Graph properties."
							+ "\n	Use -h or --help to print usage help.");
				}
				
				
				if(clHandler.getDiameter() == true) {
					System.out.println("Diameter: " + G.getDiameter());
				}
				
				
				if(clHandler.getGraphProperties() == true) {
					System.out.println(G.toString());
				}
				
				
				//TODO
//				if(clHandler.getAllShortestPaths() == true) {
//					//start calculation of all shortest paths
//				}
						
				//TODO
//				if(clHandler.getBcm() == true) {
//					System.out.println(clHandler.getBcmNodeID());
//					//start calculation of BCM
//				}
				
				//TODO
//				if(clHandler.getOutputFileName() != null) {
//					// start all calculations
//					// print all attributes and calculations into new .graphml file
//				}
				
			
				
			} else {
				throw new Exception();
			} 
		} catch (Exception e) {
			System.out.println("ERROR: Provide at least one argument."
					+ "\n	Use -h or --help to print usage help.");
		}
	
		
			
			
			
			

//		String fileName = args[0];
////		String fileName = "D:\\Benutzer\\Desktop\\Uni\\Java Project\\Graphen\\small_graph.graphml";
////		String fileName = "C:\\Users\\Fabian\\Downloads\\medium_graph.graphml";
////		String fileName = "C:\\Users\\khali\\OneDrive\\Desktop\\\\gra.xml";
//		FileHandler nFileHandler = new FileHandler();
//		nFileHandler.setGraphmlFile(fileName);
//		nFileHandler.prepareParser();

		
//		Graph G = new Graph(nFileHandler.getEdgeList(), nFileHandler.getNodeList());
//		System.out.println(G.toString());
//		G.printAllNodes();
//		G.printAllEdges();
//
//		
//		/*	Testing the shortest path algorithm
//		 * 	can be removed later
//		 */
//		System.out.println(G.shortestPath(5, 7));
//>>>>>>> ddc06d7c29562bc29416d09e6b75f8b4c5b509a7
		
	}

	
}

