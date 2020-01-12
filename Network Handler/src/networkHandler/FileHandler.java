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

/**
 * This class implements a parser for graphml files.
 * @author Sebastian Monok, Khalid Butt
 *
 */
public class FileHandler {
	private File graphmlFile;
	private ArrayList<networkHandler.Node> nodeList;
	private ArrayList<Edge> edgeList;

	
	
	/**
	 * This constructs a parser of the graphml file.
	 */
	public FileHandler() {
		this.nodeList = new ArrayList<>();
		this.edgeList = new ArrayList<>();
		this.graphmlFile = null ;
	}

	
	
	// setter - getter
	public void setGraphmlFile(String filePath) {
		this.graphmlFile = new File(filePath);
	}
	
	public ArrayList<networkHandler.Node> getNodeList() {
		return nodeList;
	}
	
	public ArrayList<Edge> getEdgeList() {
		return edgeList;
	}


	
	/**
	 * This method creates a parsable document by using {@code DocumentBuilderFactory},
	 * {@code DocumentBuilder} and {@code Document}. The parsable document is specified with the filepath of the
	 * graphml file which has to be parsed.
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public void prepareParser(){

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		Document document = null;

		try {
			document = builder.parse(graphmlFile);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		graphmlParserNodes(document);
		graphmlParserEdges(document);

	}

	// parses only the nodes
	private void graphmlParserNodes(Document document) {

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

	
	// parses only the edges
	private void graphmlParserEdges(Document document) {
		
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

