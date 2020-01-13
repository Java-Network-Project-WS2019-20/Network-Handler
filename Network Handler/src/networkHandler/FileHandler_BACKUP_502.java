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

<<<<<<< HEAD

/**
 * Class is used to handle the provided graphml file from the user. The main task of this class
 * is to create an parsable document object which points to the provided graphml file from the user
 * and also to parse the document for its nodes and edges, which are then saved into ArrayLists.
 * @author Khalid Butt
 * @author Sebastian Monok
 */

public class FileHandler {


	/**
	 * Points to the provided graphml file from the user.
	 */
=======
/**
 * This class implements a parser for graphml files.
 * @author Sebastian Monok, Khalid Butt
 *
 */
public class FileHandler {
>>>>>>> 229e1c4ddcd73c8ac6fcb1f6df19e84d33855ecf
	private File graphmlFile;

	/**
	 * Contains all nodes from the provided graphml file.
	 */
	private ArrayList<networkHandler.Node> nodeList;

	/**
	 * Contains all edges from the provided graphml file.
	 */
	private ArrayList<Edge> edgeList;

	
	
	/**
	 * This constructs a parser of the graphml file.
	 */
	public FileHandler() {
<<<<<<< HEAD

		//Init
=======
>>>>>>> 229e1c4ddcd73c8ac6fcb1f6df19e84d33855ecf
		this.nodeList = new ArrayList<>();
		this.edgeList = new ArrayList<>();
		this.graphmlFile = null ;
	}

<<<<<<< HEAD
	/**
	 * Specify the filepath of the graphml file.
	 * @param filePath
	 * Parameter is the filepath which is provided by the user of the graphml file
	 */
=======
	
	
	// setter - getter
>>>>>>> 229e1c4ddcd73c8ac6fcb1f6df19e84d33855ecf
	public void setGraphmlFile(String filePath) {
		this.graphmlFile = new File(filePath);
	}

	/**
	 * Return the ArrayList with the extracted nodes from provided the graphml file.
	 * @return <code>ArrayList</code>
	 */
	public ArrayList<networkHandler.Node> getNodeList() {
		return nodeList;
	}

	/**
	 * Return the ArrayList with the extracted edges from provided the graphml file.
	 * @return <code>ArrayList</code>
	 */
	public ArrayList<Edge> getEdgeList() {
		return edgeList;
	}


	
	/**
<<<<<<< HEAD
	 * Create a parsable document. The Document object is specified with the filepath of the
	 * graphml file which is provided by the user. The graphml file is then passed to {@link FileHandler#graphmlParserEdges(Document)}
	 * and {@link FileHandler#graphmlParserEdges(Document)} to parse the document for the nodes and edges.
=======
	 * This method creates a parsable document by using {@code DocumentBuilderFactory},
	 * {@code DocumentBuilder} and {@code Document}. The parsable document is specified with the filepath of the
	 * graphml file which has to be parsed.
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
>>>>>>> 229e1c4ddcd73c8ac6fcb1f6df19e84d33855ecf
	 */
	public void prepareParser(){

		//Create a parsable document
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

		//Pass the parsable document to nodeParser and edgeParser method
		graphmlParserNodes(document);
		graphmlParserEdges(document);

	}

<<<<<<< HEAD

	/**
	 * Parse Document object for nodes and save them into an ArrayList.
	 * This is done by extracting all nodes into a {@link NodeList} by defining the tag element "node" as parameter in
	 * the function {@link Document#getElementsByTagName(String)}.
	 * The {@link NodeList} is then iterated for each node and saved into {@link FileHandler#nodeList}.
	 * @param document The provided graphml by the user is passed to this method.
	 */
	public void graphmlParserNodes(Document document) {
=======
	// parses only the nodes
	private void graphmlParserNodes(Document document) {
>>>>>>> 229e1c4ddcd73c8ac6fcb1f6df19e84d33855ecf

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

<<<<<<< HEAD

	/**
	 * Parse Document object for edges and saves them into an ArrayList.
	 * This is done by extracting all edges into a {@link NodeList} by defining the tag element "edge" as parameter in
	 * the function {@link Document#getElementsByTagName(String)}.
	 * The {@link NodeList} is then iterated for each edge and saved into {@link FileHandler#edgeList}.
	 * @param document The provided graphml by the user is passed to this method.
	 */
	public void graphmlParserEdges(Document document) {
=======
	
	// parses only the edges
	private void graphmlParserEdges(Document document) {
		
>>>>>>> 229e1c4ddcd73c8ac6fcb1f6df19e84d33855ecf
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

