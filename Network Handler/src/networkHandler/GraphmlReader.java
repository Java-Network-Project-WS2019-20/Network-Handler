package networkHandler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
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
 * Class is used to handle the provided graphml file from the user. The main task of this class
 * is to create an parsable document object which points to the provided graphml file from the user
 * and also to parse the document for its {@link Node}s and {@link Edge}s, which are then saved into {@link ArrayList}s.
 * @author Khalid Butt
 * @author Sebastian Monok
 */

public class GraphmlReader {

	private File graphmlFile;
	private ArrayList<networkHandler.Node> nodeList;
	private ArrayList<Edge> edgeList;
	private boolean parseSuccessful = false;
	private final Logger mylog = LogManager.getLogger(GraphmlReader.class);

	/**
	 * Default Constructor
	 * Initializes necessary attributes.
	 */
	public GraphmlReader() {
		this.nodeList = new ArrayList<>();
		this.edgeList = new ArrayList<>();
		this.graphmlFile = null ;

	}

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
	 * Creates a parsable document. The Document object is specified with the filepath of the
	 * graphml file which is provided by the user. The graphml file is then passed to {@link GraphmlReader#doParseEdges(Document)}
	 * and {@link GraphmlReader#doParseEdges(Document)} to parse the document for the nodes and edges.
	 */
	public void prepareParser(){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document document = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			mylog.error("Something went wrong while creating a parsable document object! \n" +e.getMessage());
			return;
		}
		try {
			document = builder.parse(graphmlFile);
		} catch (SAXException e) {
			mylog.error("File content is not allowed \n" +e.getMessage());
			return;
		} catch (IOException e) {
			mylog.error("Can not open/find file \n" +e.getMessage());
			return;
		}
		doParseNodes(document);
		doParseEdges(document);
		mylog.debug("Parsed " + graphmlFile.getName() + " successfully.");
		parseSuccessful = true;
	}
	
	/**
	 * The method returns information about the success of parsing the given file
	 * @return boolean, true if parsed successfully, otherwise false
	 */
	public boolean isParseSuccessful() {
		return parseSuccessful;
	}
	
	/**
	 * Parse Document object for nodes and save them into an ArrayList.
	 * This is done by extracting all nodes into a {@link NodeList} by defining the tag element "node" as parameter in
	 * the function {@link Document#getElementsByTagName(String)}.
	 * The {@link NodeList} is then iterated for each node and saved into {@link GraphmlReader#nodeList}.
	 * @param document The provided graphml by the user is passed to this method.
	 */
	private void doParseNodes(Document document) {
		NodeList nodeList = document.getElementsByTagName("node");
		for (int temp = 0; temp < nodeList.getLength(); temp++) {
			Node nextNode = nodeList.item(temp);
			if (nextNode.getNodeType() == Node.ELEMENT_NODE) {
				Element ele = (Element) nextNode;
				this.nodeList.add(new networkHandler.Node(Integer.parseInt(ele.getElementsByTagName("data").item(0).getTextContent())));
			}
		}
		mylog.debug("Nodes were parsed succesfully!");
	}

	/**
	 * Parse Document object for edges and saves them into an ArrayList.
	 * This is done by extracting all edges into a {@link NodeList} by defining the tag element "edge" as parameter in
	 * the function {@link Document#getElementsByTagName(String)}.
	 * The {@link NodeList} is then iterated for each edge and saved into {@link GraphmlReader#edgeList}.
	 * @param document The provided graphml by the user is passed to this method.
	 */
	private void doParseEdges(Document document) {
		NodeList edgesNodeList = document.getElementsByTagName("edge");
		for (int temp = 0; temp < edgesNodeList.getLength(); temp++) {
			Node nextEdge = edgesNodeList.item(temp);
			if (nextEdge.getNodeType() == Node.ELEMENT_NODE) {
				Element ele = (Element) nextEdge;
				edgeList.add(new Edge(Integer.parseInt(ele.getElementsByTagName("data").item(0).getTextContent()),
						Integer.parseInt(ele.getAttribute("source").substring(1)),
						Integer.parseInt(ele.getAttribute("target").substring(1)),
						Integer.parseInt(ele.getElementsByTagName("data").item(1).getTextContent())));
			}
		}
		mylog.debug("Edges were parsed succesfully!");
	}

	
}

