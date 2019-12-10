package networkHandler;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class FileHandler {
	private File fileURI;
	private ArrayList<Node> nodeList;
	private ArrayList<Edge> edgeList;

	public FileHandler() {

		this.nodeList = new ArrayList<>();
		this.edgeList = new ArrayList<>();
		this.fileURI = null ;

	}

	public void setFileURI(String filePath) {
		this.fileURI = new File(filePath);
	}


	public void prepareParser() throws ParserConfigurationException, IOException, SAXException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(fileURI);

		parseForNodes(document);
		parseForEdges(document);

	}

	public void parseForNodes(Document document){

		// TODO
	}

	public void parseForEdges(Document document){

		// TODO

	}

}

