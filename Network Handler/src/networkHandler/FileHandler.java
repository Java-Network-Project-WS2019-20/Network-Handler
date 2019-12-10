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
	private File fileURI;
	private ArrayList<networkHandler.Node> nodeList;
	private ArrayList<Edge> edgeList;

	public FileHandler() {

		this.nodeList = new ArrayList<>();
		this.edgeList = new ArrayList<>();
		this.fileURI = null ;

	}

	public void setFileURI(String filePath) {
		this.fileURI = new File(filePath);
	}
	
	// for debugging
	public ArrayList<networkHandler.Node> getNodeList() { return nodeList; }
	public ArrayList<Edge> getEdgeList() { return edgeList; }


	public void prepareParser() throws ParserConfigurationException, IOException, SAXException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(fileURI);

		// if graph is small to medium use DOM if large use SAX
		// fileURI.length() returns file size in bytes
		if (fileURI.length() < 40000) graphmlParserUsingDOM(document);
		else graphmlParserUsingSAX(document);

	}
	
	
	// use DOM if graph size is small to medium
	public void graphmlParserUsingDOM(Document document) {
		
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
	
	
	// use SAX if graph size is large
	public void graphmlParserUsingSAX(Document document) {
		//TODO implement
	}
	

}

