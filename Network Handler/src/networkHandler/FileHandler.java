package networkHandler;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;


public class FileHandler {
	private File graphmlFile;
	private ArrayList<networkHandler.Node> nodeList;
	private ArrayList<Edge> edgeList;

	
	// constructor
	public FileHandler() {

		this.nodeList = new ArrayList<>();
		this.edgeList = new ArrayList<>();
		this.graphmlFile = null ;

	}

	
	// setter + getter
	public void setGraphmlFile(String filePath) {
		this.graphmlFile = new File(filePath);
	}
	
	public ArrayList<networkHandler.Node> getNodeList() {
		return nodeList;
	}
	
	public ArrayList<Edge> getEdgeList() {
		return edgeList;
	}


	// Instantiates the DocumentBuilderFactory for parsing
	public void prepareParser() throws ParserConfigurationException, IOException, SAXException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(graphmlFile);

		graphmlParserNodes(document);
		graphmlParserEdges(document);

	}
	
	
	// parses only the nodes of the graph
	public void graphmlParserNodes(Document document) {
		// reading all nodes into nodesNL
		NodeList nodesNL = document.getElementsByTagName("node");
		
		// parsing node information
		for (int temp = 0; temp < nodesNL.getLength(); temp++) {
			Node nNode = nodesNL.item(temp);
			
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element ele = (Element) nNode;
				nodeList.add(new networkHandler.Node(Integer.parseInt(ele.getElementsByTagName("data").item(0).getTextContent())));
			}
		}
	}
	
	
	// parses only the edges of the graph
	public void graphmlParserEdges(Document document) {
		// reading all edges into edgesNL
				NodeList edgesNL = document.getElementsByTagName("edge");
				
		//parsing edge information
		for (int temp = 0; temp < edgesNL.getLength(); temp++) {
			Node nEdge = edgesNL.item(temp);
			
			if (nEdge.getNodeType() == Node.ELEMENT_NODE) {
				Element ele = (Element) nEdge;
				edgeList.add(new Edge(Integer.parseInt(ele.getElementsByTagName("data").item(0).getTextContent()),
						Integer.parseInt(ele.getAttribute("source").substring(1)),
						Integer.parseInt(ele.getAttribute("target").substring(1)),
						Integer.parseInt(ele.getElementsByTagName("data").item(1).getTextContent())));
			}
		}
	}
	
	
	// preparing output of all calculations and attributes into a new graphml file
	public void exportGraphmlAnalysis () {
		
		// 1. creating a document
        Document document = new Document();
       
        // 2. creating root, node, child of node elements
        Element root = new Element("allShortestPaths");
        Element shortestPathElement = new Element("sPath")
							        		.setAttribute("source", "n0")
							        		.setAttribute("target", "n4");
        Element dataElement = new Element("data")
				        					.setAttribute("key", "sp_result")
				        					.setText("4");
       
        // 3. adding children to parents to root
        shortestPathElement.addContent(dataElement);
        root.addContent(shortestPathElement); // Add the child to the root element
        document.setContent(root); // add the root element as

        // 4. saving the document to specified file
        try {
            XMLOutputter outputter = new XMLOutputter();
            outputter.setFormat(Format.getPrettyFormat());
            outputter.output(document, new FileWriter("C:\\Users\\boost\\Downloads\\test_graph.graphml"));
        
            System.out.println(".graphml file created.");
            
        } catch (Exception e) {
            e.printStackTrace();
        }	
	}
	

	
}

