/*
 * TODO graphmlParser does not order node or edge information if given unordered
 * exportToGraphml() not yet implemented
 */

package networkHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GraphmlHandler {
	private String fileURI;
	private ArrayList<Node> nodeList = new ArrayList<Node>();
	private ArrayList<Edge> edgeList = new ArrayList<Edge>();
	
	
	public GraphmlHandler(String file) {
		setFileURI(file);
		graphmlParser();
	}


	public String getFileURI() { return fileURI; }
	public void setFileURI(String fileToRead) { this.fileURI = fileToRead; }
	
	public ArrayList<Node> getNodeList() { return nodeList; }
	public void setNodeList(ArrayList<Node> nodeList) { this.nodeList = nodeList; }
	
	public ArrayList<Edge> getEdgeList() { return edgeList; }
	public void setEdgeList(ArrayList<Edge> edgeList) { this.edgeList = edgeList; }
	
	
	public void graphmlParser() {
		// edge informations will be gathered in lists to later save them ordered in the edgeMap
		List<Integer> edgeIdL = new ArrayList<Integer>();
		List<Integer> edgeSourceL = new ArrayList<Integer>();
		List<Integer> edgeTargetL = new ArrayList<Integer>();
		List<Integer> edgeWeightL = new ArrayList<Integer>();
		
		try {
			// load graphml into a BufferdReader to search it for graph information line by line
			BufferedReader reader = Files.newBufferedReader(Paths.get(getFileURI()));
			String line;
			
			// pattern to filter numbers in graphml file
			Pattern pattern = Pattern.compile("([0-9]+)");
			
			// read all lines to find graph information
			while((line = reader.readLine()) != null) { 
				Matcher matcher = pattern.matcher(line);
				
				// find line containing node id
            	if(line.contains("id=\"n")) {
            		if(matcher.find()) {
                        int nodeID = Integer.valueOf(matcher.group(1));
                        // save Nodes into nodeList
                        getNodeList().add(new Node(nodeID));
            		}
            	}
            	
            	// find edges:
            	// find line containing edge source & target
            	if(line.contains("<edge source=")) { 
            		// source
                    if(matcher.find()) {
                        edgeSourceL.add(Integer.valueOf(matcher.group(1)));
                        // target
                        if(matcher.find()) {
                        	edgeTargetL.add(Integer.valueOf(matcher.group(1)));
                        }
                    }
            	}
            	
            	// find line containing edge ID
            	if(line.contains("<data key=\"e_id\">")) {
            		if(matcher.find()) {
            			edgeIdL.add(Integer.valueOf(matcher.group(1)));
            		}
            	}
            	
            	//find line containing edge weight
            	if(line.contains("<data key=\"e_weight\">")) {
            		if(matcher.find()) {
            			edgeWeightL.add(Integer.valueOf(matcher.group(1)));
            		}
            	}
            }	
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		
		// save edges with relating information in edgeList
		for(int i=0; i<edgeIdL.size() ;i++) {
			getEdgeList().add(new Edge(edgeIdL.get(i), edgeSourceL.get(i), edgeTargetL.get(i), edgeWeightL.get(i)));
		}
	}
	
	
}

