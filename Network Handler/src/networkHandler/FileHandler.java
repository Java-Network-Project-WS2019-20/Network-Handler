package networkHandler;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
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
	
	
}

