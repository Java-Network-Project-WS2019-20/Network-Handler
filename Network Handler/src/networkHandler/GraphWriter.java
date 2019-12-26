package networkHandler;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


public class GraphWriter {
	private String outputFileName;
	private int bcm;
	private boolean connectivity;
	private int diameter;
	private HashMap<Integer, HashMap<ArrayList<Integer>,ArrayList<Integer>>> spMap;	
//TODO	private boolean graphProperties;

	
	// constructor
	public GraphWriter(String outputFileName) {
		this.outputFileName = outputFileName;
		this.bcm = 0;
		this.connectivity = false;
		this.diameter = 0;
		this.spMap = new HashMap<Integer, HashMap<ArrayList<Integer>,ArrayList<Integer>>>();
	}
	
	
	// getter - setter
	public void setBcm(int bcm) { this.bcm = bcm; }
	public void setConnectivity() { this.connectivity = true; }
	public void setDiameter(int diameter) { this.diameter = diameter; }
	public void setSpMap(HashMap<Integer, HashMap<ArrayList<Integer>,ArrayList<Integer>>> shortestPathMap) {
		this.spMap = new HashMap<Integer, HashMap<ArrayList<Integer>,ArrayList<Integer>>>(shortestPathMap);
	}
	
	
	// preparing output of all calculations and attributes into new file
	public void exportGraphmlAnalysis () {
		
		// 1. creating a document
//        Document document = new Document();
//       
//        // 2. creating root, node, child of node elements
//        Element root = new Element("allShortestPaths");
//        Element shortestPathElement = new Element("sPath")
//							        		.setAttribute("source", "n0")
//							        		.setAttribute("target", "n4");
//        Element dataElement = new Element("data")
//				        					.setAttribute("key", "sp_result")
//				        					.setText("4");
//       
//        // 3. adding children to parents to root
//        shortestPathElement.addContent(dataElement);
//        root.addContent(shortestPathElement); // Add the child to the root element
//        document.setContent(root); // add the root element as
//
//        // 4. saving the document to specified file
//        try {
//            XMLOutputter outputter = new XMLOutputter();
//            outputter.setFormat(Format.getPrettyFormat());
//            outputter.output(document, new FileWriter("C:\\Users\\boost\\Downloads\\test_graph.graphml"));
//        
//            System.out.println(".graphml file created.");
//            
//        } catch (Exception e) {
//            e.printStackTrace();
//        }   

		

        
        // iterate and display keys
		
		
		

		

		
		
	}
	
	
	
	
	
}

