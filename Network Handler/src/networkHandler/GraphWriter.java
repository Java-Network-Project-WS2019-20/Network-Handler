package networkHandler;

import java.io.FileWriter;
import java.util.ArrayList;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class GraphWriter {
	private String outputFileName;
	private Graph graph;
	private ArrayList<ArrayList<ArrayList<Path>>> shortestPathList;
	
	
	
	// constructor
	public GraphWriter(String outputFileName, Graph graph) {
		this.outputFileName = outputFileName;
		this.graph = graph;
		this.shortestPathList = graph.shortestPaths();
	}
	
	

	// write all calculations and attributes of the parsed graph into new file
	// A. create all elements of the document
	public void exportGraphmlAnalysis() {
		
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
        
        
    // B. write document to new file   
	private void writeToOutputfile(Document document) {
        try {
            XMLOutputter outputter = new XMLOutputter();
            outputter.setFormat(Format.getPrettyFormat());
            outputter.output(document, new FileWriter(outputFileName));
        
            System.out.println("File created: " + outputFileName);
            
        } catch (Exception e) {
            e.printStackTrace();
        }   	
	} 

	
}

