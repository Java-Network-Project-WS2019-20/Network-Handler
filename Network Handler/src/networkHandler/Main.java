package networkHandler;

import org.apache.commons.cli.ParseException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

public class Main {

	public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {

		
		// cla (command line arguments) you may use for testing:
		
		// C:\\Users\\Krzysztof\\Desktop\\Uni\\Java Project\\Graphen\\medium_graph.graphml -a /outputfile.graphml -b 2 -c -d -s 1 14
		// C:\\Users\\Fabian\\Downloads\\medium_graph.graphml -a /outputfile.graphml -b 2 -c -d -s 1 14
		// C:\\Users\\khali\\OneDrive\\Desktop\\\\gra.xml -b 2 -c -d -s 1 14
		// C:\\Users\\boost\\Downloads\\small_graph.graphml -a /outputfile.graphml -b 2 -c -d -s 1 14
		
//		try {
			if (args.length > 0) {	// user must provide at least one argument = input filename
				
				
				// start parsing cla
				CommandLineHandler clHandler = new CommandLineHandler(args);
				clHandler.claParser();

				
				// start parsing the graph
				FileHandler nFileHandler = new FileHandler();
				if(clHandler.getInputFileName() != null) {
					nFileHandler.setGraphmlFile(clHandler.getInputFileName());
					nFileHandler.prepareParser();
				}
				
				
				// create Graph object
				Graph G = new Graph(nFileHandler.getEdgeList(), nFileHandler.getNodeList());
	
				
				// retrieving and forwarding results of cla parsing
				
				// check connectivity
				if(clHandler.getConnectivity() == true) {
					//System.out for testing only
					System.out.println("Graph is connected: " + G.isGraphConnected());
				}
				
				// get shortestPath between two provided Nodes
				try {
					if(clHandler.getShortestPath() == true) {
						//System.out for testing only
						System.out.println( "Shortest Path between: "
								+ clHandler.getShortestPathNodeIDs()[0] + " and "
								+ clHandler.getShortestPathNodeIDs()[1] + " = "
								+ G.shortestPath(clHandler.getShortestPathNodeIDs()[0], clHandler.getShortestPathNodeIDs()[1]).getLength());
					}
			
				// if user provides non existing Node ID's:
				} catch (NoSuchElementException nsee) {
					System.out.println("ERROR: Can not calculate shortest path."
							+ "\n	The Node ID you provided does not exist."
							+ "\n	use -G or --graph to display Graph properties."
							+ "\n	Use -h or --help to print usage help.");
				}
				
				// check diameter
				if(clHandler.getDiameter() == true) {
					System.out.println("Diameter: " + G.getDiameter());
				}
				
				// prints out # of nodes and edges
				if(clHandler.getGraphProperties() == true) {
					System.out.println(G.toString());
				}
				
				// get all shortest paths between every node
				if(clHandler.getAllShortestPaths() == true) {
					G.getAllShortestPaths();
					//TODO System.out.
				}
				
				// creates new .graphml file with all attributes and calculations
				if(clHandler.getOutputFileName() != null) {
					GraphWriter gw = new GraphWriter(clHandler.getOutputFileName());
					gw.setNodeList(nFileHandler.getNodeList());
					gw.setEdgeList(nFileHandler.getEdgeList());
					gw.setConnectivity(G.isGraphConnected());
					gw.setDiameter((int) G.getDiameter());
					gw.setSpMap(G.getAllShortestPaths());
					gw.exportGraphmlAnalysis();
				}
				
				
				
				
				//TODO
//				if(clHandler.getBcm() == true) {
//					System.out.println(clHandler.getBcmNodeID());
//					//start calculation of BCM
//				}
				
			
				
				
				
//			} else {
//				throw new Exception();
//			} 
//		} catch (Exception e) {
//			System.out.println("ERROR: Provide at least one argument."
//					+ "\n	Use -h or --help to print usage help.");
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

		
	}

	
}

