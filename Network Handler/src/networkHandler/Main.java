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
		
		try {
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
//				System.out.println(G.toString());
//				G.printAllNodes();
//				G.printAllEdges();
				
				
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
								+ G.shortestPath(clHandler.getShortestPathNodeIDs()[0], clHandler.getShortestPathNodeIDs()[1]));
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
				
				//TODO retrieve graph properties = nodes and edges 
				if(clHandler.getGraphProperties() == true) {
					System.out.println(G.toString());
				}
				
				
				//TODO update: use multiMap
				// get all shortest paths between every node
				if(clHandler.getAllShortestPaths() == true) {
//					G.getAllShortestPaths();
					
//					HashMap<Integer, ArrayList<Integer>> spMap = new HashMap(G.getAllShortestPaths());
	
					GraphWriter gw = new GraphWriter("C:\\Users\\boost\\Downloads\\test.graphml");
					gw.setSpMap(G.getAllShortestPaths());
					gw.exportGraphmlAnalysis();
//					// iterate and display values
//					for(Entry<Integer, ArrayList<Integer>> entry : spMap.entrySet()) {
//						int key = entry.getKey();
//						ArrayList<Integer> values = entry.getValue();
//						
//						System.out.println("Key = " + key);
//						System.out.println("Values = " + values);
//					}
				}
					
				
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

		
	}

	
}

