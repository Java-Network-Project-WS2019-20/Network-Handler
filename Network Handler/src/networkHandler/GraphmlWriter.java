package networkHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.Iterator;
import java.util.Scanner;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


/**
 * This class implements the output of all calculations done over a graph into a new file.
 * @author Sebastian Monok
 */

public class GraphmlWriter implements Runnable {
	private String inputFileName;
	private String outputFileName;
	private Graph graph;
	private GraphHandler graphHandler;
	private boolean createFile;
	private final Logger mylog = LogManager.getLogger(GraphmlWriter.class);

	/**
	 * This constructs a GraphWriter which writes all graph attributes and calculations over it into a new file.
	 * @param inputFileName The input filename of the given .graphml file
	 * @param outputFileName The output filename (and path) of the new file
	 * @param graphHandler The graph on which all calculations will be calculated
	 */
	public GraphmlWriter(String inputFileName, String outputFileName, GraphHandler graphHandler) {
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
		this.graphHandler = graphHandler;
		this.graph = graphHandler.getGraph();
		this.createFile = true;
	}
	
	/**
	 * If the boolean createFile is set to true (due to parsing the command line arguments)
	 * this method will initiate the export of the calculations done over the graph.
	 */
	public void run() {
		if (createFile) {
			doExportGraphAnalysis();
		}
	}
	
	
	/**
	 * This Method creates a new graphml file by using the external library jdom-2.0.6.
	 */
	public void doExportGraphAnalysis() {
		// 1. creating a document
        Document document = new Document();
        // 2. creating root, node, child of node elements
        Element rootElement = new Element("graphml")
        		.setAttribute("created.with", "https://github.com/Java-Network-Project-WS2019-20");
        Element keyElement;
        Element inputFileElement = new Element("inputFile").setAttribute("name", inputFileName);
        Element graphElement = new Element("graph")
        		.setAttribute("id", "G")
        		.setAttribute("edgedefault", "undirected");
        Element connectivityElement = new Element("connectivity")
        		.setAttribute("connected", String.valueOf( graphHandler.getConnectivityValue() ));
        Element diameterElement = new Element("diameter")
        		.setAttribute("diameter", String.valueOf( graphHandler.getDiameterValue() ));
        Element nodesElement = new Element("nodes");
        Element numberOfNodes = new Element("Nodes")
        		.setAttribute("amount", String.valueOf(graphHandler.getGraph().getNodeCount()));
  		Element numberOfEdges = new Element("Edges")
  				.setAttribute("amount", String.valueOf(graphHandler.getGraph().getEdgeCount()));
 		Element dataElement;
  		Element nodeElement;
        Element edgesElement = new Element("edges");
        Element edgeElement;	
        Element allShortestPathsElement = new Element("shortestPaths");
        Element shortestPathElement;
        Element allBetweennessCentralityMeasureElement = new Element("betweennessCentralityMeasures");
        Element betweennessCentralityMeasureElement;
 		Element minimumSpanningTree = new Element("minumumSpanningTree");
 		// 2.a. creating node elements
 		for (int i=0; i < graph.getNodeCount(); i++) {
 			nodeElement = new Element("node")
 					.setAttribute("id", "n" + String.valueOf(graph.getNodeList().get(i).getID()))
			 		.addContent(dataElement = new Element("data")
		 			.setAttribute("key", "v_id")
		 			.setText(String.valueOf(graph.getNodeList().get(i).getID())));
 			nodesElement.addContent(nodeElement);
 		}
 		// 2.b creating edge elements
 		for (int i=0; i < graph.getEdgeCount(); i++) {
 			edgeElement = new Element("edge")
			 		.setAttribute("source", "n" + String.valueOf(graph.getEdgeList().get(i).getSourceNodeId()))
			 		.setAttribute("target", "n" + String.valueOf(graph.getEdgeList().get(i).getTargetNodeId()))
			 		.addContent(dataElement = new Element("data")
		 			.setAttribute("key", "e_id")
		 			.setText(String.valueOf(graph.getEdgeList().get(i).getEdgeID())))
			 		.addContent(dataElement = new Element("data")
					.setAttribute("key", "e_weight")
					.setText(String.valueOf((int) graph.getEdgeList().get(i).getWeight())));
 			edgesElement.addContent(edgeElement);
 		}
        // 2.c. creating shortest path elements
 		Iterator<Path>	pathIterator = graphHandler.getShortestPathsListAllValue().iterator();
 		while(pathIterator.hasNext()) {
 			Path currentPath = pathIterator.next();
			shortestPathElement = new Element("shortestPath");
			dataElement = new Element("data");
			shortestPathElement.setAttribute("source", "n" + currentPath.getOriginNode());
			shortestPathElement.setAttribute("target", "n" + currentPath.getDestinationNode());
			dataElement.setAttribute("key", "length").setText(String.valueOf(currentPath.getLength()));
			shortestPathElement.addContent(dataElement);
			if(currentPath.getLength() != Double.POSITIVE_INFINITY) {
    			for(int j = 0; j < currentPath.getNumberOfNodes(); j++) {
    				dataElement = new Element("data");
    				dataElement.setAttribute("key", "segment" + j).setText("n" + currentPath.getNode(j));
    				shortestPathElement.addContent(dataElement);
    			}
			}
			allShortestPathsElement.addContent(shortestPathElement);
 		}
 		// 2.d. creating betweennessCentralityMeasure elements
 		for (int i=0; i < graphHandler.getAllBetweennessCentralityMeasuresValue().size(); i++) {
 			betweennessCentralityMeasureElement = new Element("bcm")
 					.setAttribute("node", "n" + String.valueOf(i))
 					.addContent(dataElement = new Element("data"))
 					.setAttribute("key", "n_bcm")
 					.setText(String.valueOf(graphHandler.getAllBetweennessCentralityMeasuresValue().get(i).getValue()));
 			allBetweennessCentralityMeasureElement.addContent(betweennessCentralityMeasureElement);
 		}
        // 2.e. creating minimumSpanningTree elements
 		for (int i=0; i < graphHandler.getMinimumSpanningTreeValue().size(); i++) {
 			edgeElement = new Element("edge")
 					.setAttribute("source", "n" + String.valueOf(graphHandler.getMinimumSpanningTreeValue().get(i).getSourceNodeId()))
					.setAttribute("target", "n" + String.valueOf(graphHandler.getMinimumSpanningTreeValue().get(i).getTargetNodeId()))
					.addContent(dataElement = new Element("data")
					.setAttribute("key", "e_id")
					.setText(String.valueOf(graphHandler.getMinimumSpanningTreeValue().get(i).getEdgeID())))
					.addContent(dataElement = new Element("data")
					.setAttribute("key", "e_weight")
					.setText(String.valueOf((int) graphHandler.getMinimumSpanningTreeValue().get(i).getWeight())));
 			minimumSpanningTree.addContent(edgeElement);	
 		}  
        // 3. adding all children to parents to root
		graphElement.addContent(numberOfNodes);
		graphElement.addContent(numberOfEdges);
 		graphElement.addContent(connectivityElement);
 		graphElement.addContent(diameterElement);
		graphElement.addContent(nodesElement);
 		graphElement.addContent(edgesElement);
 		graphElement.addContent(allShortestPathsElement);
 		graphElement.addContent(allBetweennessCentralityMeasureElement);
 		graphElement.addContent(minimumSpanningTree);
 		rootElement.addContent(keyElement = new Element("key")
 		.setAttribute("id", "v_id")
 		.setAttribute("for", "node")
 		.setAttribute("attr.name", "id")
 		.setAttribute("attr.type", "int"))
		.addContent(keyElement = new Element("key")
		.setAttribute("id", "e_id")
		.setAttribute("for", "edge")
		.setAttribute("attr.name", "id")
		.setAttribute("attr.type", "int"))
		.addContent(keyElement = new Element("key")
		.setAttribute("id", "e_weight")
		.setAttribute("for", "edge")
		.setAttribute("attr.name", "weight")
		.setAttribute("attr.type", "int"))
		.addContent(keyElement = new Element("key")
		.setAttribute("id", "length")
		.setAttribute("for", "shortestPath")
		.setAttribute("attr.name", "length")
		.setAttribute("attr.type", "int"))
		.addContent(keyElement = new Element("key")
		.setAttribute("id", "segment")
		.setAttribute("for", "shortestPath")
		.setAttribute("attr.name", "segment")
		.setAttribute("attr.type", "int"))
 		.addContent(keyElement = new Element("key")
 		.setAttribute("id", "n_bcm")
 		.setAttribute("for", "bcm")
		.setAttribute("attr.name", "bcm")
		.setAttribute("attr.type", "double"));
 		rootElement.addContent(inputFileElement);
 		rootElement.addContent(graphElement);
        document.setContent(rootElement);
        doWriteToOutputfile(document);
	}
    // write document to new file
	private void doWriteToOutputfile(Document document) {  
        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(Format.getPrettyFormat());
        try {
			outputter.output(document, new FileWriter(outputFileName));
		} catch (IOException e) {
        	mylog.error("Can not create file at specified path. \n" +e.getMessage());
		}
        mylog.info("File created: " + outputFileName);
	}
	
	// check if file already exists
	public void doCheckIfFileExists() {
		File outputFileExist = new File(outputFileName);
		Scanner in = new Scanner(System.in);
		try {
	        if (outputFileExist.exists()) {
	        	throw new FileAlreadyExistsException(outputFileName);	
	        }
        } catch (FileAlreadyExistsException e1) {
			mylog.info("File already exists. Continue? YES/NO/RENAME: ");
        	String userDecision = in.nextLine();
        	if (userDecision.equalsIgnoreCase("YES")) {
        		// continue
        	} else if (userDecision.equalsIgnoreCase("RENAME")) {
        		// rename outputFileName to newFileName
				System.out.println("Enter new file name and path: ");
            	String newFileName = in.nextLine();
            	this.outputFileName = newFileName;
            	mylog.info("New file name: " + newFileName);
            	// check new name
            	doCheckIfFileExists();
        	} else {
        		// else do not create file
        		createFile = false;
        	}
        } finally {
        	in.close();
        }
	}
}
