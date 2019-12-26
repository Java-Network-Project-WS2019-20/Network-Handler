package networkHandler;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;


public class GraphWriter {
	private String outputFileName;
	private int bcm;
	private boolean connectivity;
	private int diameter;
	private MultiValuedMap<Integer, ArrayList<Integer>> spMap;	
//TODO	private boolean graphProperties;

	
	// constructor
	public GraphWriter(String outputFileName) {
		this.outputFileName = outputFileName;
		this.bcm = 0;
		this.connectivity = false;
		this.diameter = 0;
		this.spMap = new ArrayListValuedHashMap<>();
	}
	
	
	
	// getter - setter
	public void setBcm(int bcm) { this.bcm = bcm; }
	public void setConnectivity() { this.connectivity = true; }
	public void setDiameter(int diameter) { this.diameter = diameter; }
	public void setSpMap(MultiValuedMap<Integer, ArrayList<Integer>> shortestPathMap) { this.spMap = shortestPathMap; }
	
	
	
	// preparing output of all calculations and attributes into new file
	public void exportGraphmlAnalysis () {
		
		// 1. creating a document
        Document document = new Document();
       
        // 2. creating root, node, child of node elements
        
        
        
        	// 2.a. creating shortest path elements
        Element root = new Element("allShortestPaths");
        
        		// Retrieving data of multiMap containing shortest paths data
        		// Iterate through the key set
 		Set<Integer> keys = spMap.keySet();
 		
 		Element shortestPathElement;
 		Element dataElement;
 		
 		for (Integer key : keys) {

				// Retrieving both ArrayList of one key
				// by iterating through the key values
			Iterator<ArrayList<Integer>> it = spMap.get(key).iterator();
			
				// Populating ArrayLists with the key values
			ArrayList<Integer> multiMapKeyTargets = new ArrayList<Integer>();
			ArrayList<Integer> multiMapKeySpResults = new ArrayList<Integer>();
			
			while (it.hasNext()) {
				multiMapKeyTargets.addAll(it.next());
				multiMapKeySpResults.addAll(it.next());
			}
			
				// adding source, target, sp_result values by iterating through ArrayLists in 2nd loop
			for (int i = 0; i < multiMapKeyTargets.size(); i++) { 
				
				shortestPathElement = new Element("sPath");
	 	 		dataElement = new Element("data");
	 			
	 	 			// adding source value
	 			shortestPathElement.setAttribute("source", key.toString());
				
	 				// adding target, sp_result values
				shortestPathElement.setAttribute("target", multiMapKeyTargets.get(i).toString());  
				dataElement.setAttribute("key", "sp_result").setText(multiMapKeySpResults.get(i).toString());
	            
					// adding created node elements to root
				shortestPathElement.addContent(dataElement);
				root.addContent(shortestPathElement);
			}  
 		}
        

        // 3. adding all children to parents to root
        document.setContent(root); // add the root element as

        // 4. saving the document to specified file
        try {
            XMLOutputter outputter = new XMLOutputter();
            outputter.setFormat(Format.getPrettyFormat());
//            outputter.output(document, new FileWriter("C:\\Users\\boost\\Downloads\\test.graphml"));
            outputter.output(document, new FileWriter(outputFileName));
        
            System.out.println("File created: " + outputFileName);
            
        } catch (Exception e) {
            e.printStackTrace();
        }   


		
	} // completing exportGraphmlAnalysis()

	
} // completing class body

