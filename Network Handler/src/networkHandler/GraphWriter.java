package networkHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.util.ArrayList;
import java.util.Scanner;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * This class implements the output of all calculations done over a graph into a new file.
 * @author Sebastian Monok
 *
 */
public class GraphWriter {
	private String outputFileName;
	private Graph graph;
	private ArrayList<ArrayList<ArrayList<Path>>> shortestPathList;
	
	
	
	/**
	 * This constructs a GraphWriter which writes all graph attributes and calculations over it into a new file.
	 * @param outputFileName The output filename (and path) of the new file
	 * @param graph The graph on which all calculations will be calculated
	 */
	public GraphWriter(String outputFileName, Graph graph) {
		this.outputFileName = outputFileName;
		this.graph = graph;
		this.shortestPathList = graph.shortestPaths();
	}
	
	
	
	/**
	 * This Method creates new graphml file by using the external library jdom-2.0.6.
	 */
	public void exportGraphmlAnalysis() {
		
		// check if provided output file already exists
		checkFileExists();
		
		// 1. creating a document
        Document document = new Document();
       
        // 2. creating root, node, child of node elements
        Element rootElement = new Element("graphml")
        		.setAttribute("created.with", "https://github.com/Java-Network-Project-WS2019-20");
        Element keyElement;
        Element graphElement = new Element("graph")
        		.setAttribute("id", "G")
        		.setAttribute("edgedefault", "undirected");
        Element connectivityElement = new Element("connectivity")
        		.setAttribute("connected", String.valueOf( graph.getConnectivity() ));
        Element diameterElement = new Element("diameter")
        		.setAttribute("diameter", String.valueOf( graph.getDiameter() ));
        Element nodesElement = new Element("nodes");
        Element nodeElement;
        Element edgesElement = new Element("edges");
        Element edgeElement;	
        Element allShortestPathsElement = new Element("shortestPaths");
        Element shortestPathElement;
 		Element dataElement;
        
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
 					.setAttribute("source", "n" + String.valueOf(graph.getEdgeList().get(i).getSource()))
 					.setAttribute("target", "n" + String.valueOf(graph.getEdgeList().get(i).getTarget()))
 					.addContent(dataElement = new Element("data")
 						.setAttribute("key", "e_id")
 						.setText(String.valueOf(graph.getEdgeList().get(i).getEdgeID())))
 					.addContent(dataElement = new Element("data")
						.setAttribute("key", "e_weight")
						.setText(String.valueOf((int) graph.getEdgeList().get(i).getWeight())));
 			edgesElement.addContent(edgeElement);
 		}
 		
        // 2.c. creating shortest path elements
        for(int sourceId = 0; sourceId < shortestPathList.size(); sourceId++) {
        	for(int targetId = 0; targetId < shortestPathList.get(sourceId).size(); targetId++) {
        		for(int i = 0; i < shortestPathList.get(sourceId).get(targetId).size(); i++) {
        			//	get Path 
        			Path currentShortestPath = shortestPathList.get(sourceId).get(targetId).get(i);
        			
        			shortestPathElement = new Element("shortestPath");
        			dataElement = new Element("data");
        			
        			//	adding source value
        			shortestPathElement.setAttribute("source", "n" + sourceId);
        			
        			//	adding target value
        			shortestPathElement.setAttribute("target", "n" + targetId);
        			
        			//	adding length value
        			dataElement.setAttribute("key", "length").setText(String.valueOf(currentShortestPath.getLength()));
        			shortestPathElement.addContent(dataElement);
        			
        			//	adding segment values
        			if(currentShortestPath.getLength() != Double.POSITIVE_INFINITY) {
	        			for(int j = 0; j < currentShortestPath.getNumberOfNodes(); j++) {
	        				dataElement = new Element("data");
	        				dataElement.setAttribute("key", "segment" + j).setText("n" + currentShortestPath.getNode(j));
	        				//	adding created node elements to root
	        				shortestPathElement.addContent(dataElement);
	        			}
        			}
        			
        			//	shortestPathElement.addContent(dataElement);
        			allShortestPathsElement.addContent(shortestPathElement);
        		}
        	}
        }

        
        // 3. adding all children to parents to root
 		graphElement.addContent(connectivityElement);
 		graphElement.addContent(diameterElement);
		graphElement.addContent(nodesElement);
 		graphElement.addContent(edgesElement);
 		graphElement.addContent(allShortestPathsElement);
 		  
 		rootElement.addContent(keyElement = new Element("key")
 									.setAttribute("id", "v_id")
 									.setAttribute("for", "node")
 									.setAttribute("attr.name", "id")
 									.setAttribute("attr.type", "double"))
					.addContent(keyElement = new Element("key")
									.setAttribute("id", "e_id")
									.setAttribute("for", "edge")
									.setAttribute("attr.name", "id")
									.setAttribute("attr.type", "double"))
					.addContent(keyElement = new Element("key")
									.setAttribute("id", "e_weight")
									.setAttribute("for", "edge")
									.setAttribute("attr.name", "weight")
									.setAttribute("attr.type", "double"))
					.addContent(keyElement = new Element("key")
									.setAttribute("id", "length")
									.setAttribute("for", "shortestPath")
									.setAttribute("attr.name", "length")
									.setAttribute("attr.type", "double"))
			 		.addContent(keyElement = new Element("key")
									.setAttribute("id", "segment")
									.setAttribute("for", "shortestPath")
									.setAttribute("attr.name", "segment")
									.setAttribute("attr.type", "double"));
 		rootElement.addContent(graphElement);
 
        document.setContent(rootElement);

        writeToOutputfile(document);
	}
        
        
    // write document to new file
	private void writeToOutputfile(Document document) {     
        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(Format.getPrettyFormat());
        
        try {
			outputter.output(document, new FileWriter(outputFileName));
		} catch (IOException e) {
			System.out.println("ERROR: Can not create file at specified path.");
			System.exit(0);
		}
  
        System.out.println("File created: " + outputFileName);      	
	} 

	
	// check if file already exists
	private void checkFileExists() {
		File outputFileExist = new File(outputFileName);
		Scanner in = new Scanner(System.in);
    	
		try {
	        if (outputFileExist.exists()) {
	        	throw new FileAlreadyExistsException(outputFileName);	
	        }
        } catch (FileAlreadyExistsException e1) {
        	System.out.print("WARNING: File already exists. Continue? YES/NO/RENAME: ");
        	String userDecision = in.nextLine();
        	
        	if (userDecision.equalsIgnoreCase("YES")) {
        		// continue
        	} else if (userDecision.equalsIgnoreCase("RENAME")) {
        		// rename outputFileName to newFileName
        		System.out.print("Enter new file name and path: ");
            	String newFileName = in.nextLine();
        		
            	this.outputFileName = newFileName;
        		System.out.println("New file name: " + newFileName);
        	} else {
        		// else exit
        		System.exit(0);
        	}
        } finally {
        	in.close();
        }
	}
	
	
}

