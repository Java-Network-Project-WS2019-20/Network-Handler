package networkHandler;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineHandler {
	private String[] claArgs;
	private Graph graph;
	
	
	
	// constructor
	public CommandLineHandler(String[] args) {
		this.claArgs = args;
	}


	
	// handle the cla (command line arguments) in three stages:
	// 1. definition stage: creating the options
	public void claParser() {
		Option option_a = Option.builder("a")
	    		.hasArg()
	    		.argName("outputfile.graphml")
	    		.desc("Print all graph calculations and node/edge properties to a new *.graphml file. "
	    				+ "\nProvide file name and path.")
	    		.build();
	    
		Option option_b = Option.builder("b")
				.hasArg()
				.argName("node_id")
				.desc("Calculate the betweenness centrality measure for a selected node."
						+ "\nProvide a Node id.")
				.build();
		
		Option option_c = Option.builder("c")
				.desc("Checks whether the the graph is connected.")
				.build();
		
		Option option_d = Option.builder("d")
				.desc("Calculate diameter of the graph.")
				.build();
		
		Option option_s = Option.builder("s")
				.hasArgs()
				.numberOfArgs(2)
				.argName("1st node_id 2nd node_id")
				.desc("Calculate the shortest path between two vertices according to the Dijkstra algorithm."
						+ "\nProvide two Node id's seperated by a blank space.")
				.build();
		
		Option option_S = Option.builder("S")
				.desc("Calculate all shprtest paths betweem all nodes.")
				.build();
		
		Option option_G = Option.builder("G")
				.longOpt("graph")
				.desc("Print # of nodes and edges.")
				.build();
		
		Option option_help = Option.builder("h")
				.longOpt("help")
				.desc("Print this help text.")
				.build();
			
		Options options = new Options();
		options.addOption(option_a);
		options.addOption(option_b);
		options.addOption(option_c);
		options.addOption(option_d);
		options.addOption(option_s);
		options.addOption(option_S);
		options.addOption(option_G);
		options.addOption(option_help);
		
		claParserParsing(options);
	}
	
	
	// 2. parsing stage: creating the parser
	private void claParserParsing(Options options) {
		try {
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd;
			cmd = parser.parse(options, claArgs);
			
			claParserInterrogation(options, cmd);
		} catch (ParseException e) {
	    	System.out.println("ERROR: You provided (a) incorrect option(s) and/or missing option value(s)."
	    			+ "\n	Use -h or --help to print usage help.");
		}
	}
	
	
	// 3. interrogation stage: check whether options available and passing on
	private void claParserInterrogation(Options options, CommandLine cmd) {
		
		try {
			
			// if first provided argument is .graphml file
			// start parsing the graph
			if( claArgs[0].contains(".graphml") ) {
				
				//TODO	check if file exists ( maybe at the FileHandler )
				
				String inputFileName = claArgs[0];	// set inputFileName = first provided cla		
				parseGraph(inputFileName);
				
			} else if ( claArgs[0].contains("help") || ( (claArgs[0].endsWith("h")) && (cmd.hasOption('h')) ) ) {
				printHelp(options);		// if single argument is -h or --help print help text
				System.exit(0);			// and exit
			} else {
				throw new Exception();	// if user did not provide any arguments OR provided wrong arguments
			}
		
			
			// if user provided -a call GraphWriter
		    if (cmd.hasOption("a")) {
		    	GraphWriter gw = new GraphWriter(cmd.getOptionValue('a'), graph);
		    	gw.exportGraphmlAnalysis();
		    }
		
		    
		    // if user provided -b print BCM to sys.out
		    // test if value of 'b' is digit	    
		    if (cmd.hasOption("b")) {
		    	if( Character.isDigit(cmd.getOptionValue('b').charAt(0)) ) {
//			        int bcmNodeID = Integer.parseInt(cmd.getOptionValue('b'));
		    		
		    		// TODO print out BCM
		    		
			    } else {
		        	throw new ParseException("argument is not a number.");
				}
		    }
	    
		    
		    // if user provided -c print connectivity to sys.out
		    if (cmd.hasOption('c')) {
		        System.out.println("Graph is connected: " + graph.isGraphConnected());
		    }
		
		    
		    // if user provided -d print diameter to sys.out
		    if (cmd.hasOption('d')) {
		    	System.out.println("Diameter: " + graph.getDiameter());
		    }
		
		
		    // if user provided -s calculate shortest path between two given nodes and print to sys.out
		    // test if both values of 's' are digits		    
			if (cmd.hasOption('s')) {
				if( Character.isDigit(cmd.getOptionValues("s")[0].charAt(0))
			    		&& Character.isDigit(cmd.getOptionValues("s")[1].charAt(0)) ) {
			       
					try {
						System.out.println( "Shortest Path between: "
								+ cmd.getOptionValues("s")[0] + " and "
								+ cmd.getOptionValues("s")[1] + " = "
								+ graph.shortestPath( Integer.parseInt(cmd.getOptionValues("s")[0]),
														Integer.parseInt(cmd.getOptionValues("s")[1]) ).getLength() );
						
					// if user provides non existing Node ID's:
					} catch (NoSuchElementException nsee) {
						System.out.println("ERROR: Can not calculate shortest path."
								+ "\n	The Node ID you provided does not exist."
								+ "\n	use -G or --graph to display Graph properties."
								+ "\n	Use -h or --help to print usage help.");
					}
			        
			    } else {
		        	throw new ParseException("argument is not a number.");
				}
			}
		    
			
			// if user provided -G print graph to sys.out
			if (cmd.hasOption('G')) {
				System.out.println(graph);
			}
			
			
			// if user provided -S calculate all shortest paths and print to sys.out
			if (cmd.hasOption('S')) {
				ArrayList<ArrayList<ArrayList<Path>>> shortestPathList = new ArrayList<ArrayList<ArrayList<Path>>>(graph.shortestPaths());
				
				for(int sourceId = 0; sourceId < shortestPathList.size(); sourceId++) {
		        	for(int targetId = 0; targetId < shortestPathList.get(sourceId).size(); targetId++) {
		        		for(int i = 0; i < shortestPathList.get(sourceId).get(targetId).size(); i++) {
		        			Path currentShortestPath = shortestPathList.get(sourceId).get(targetId).get(i);
		        			
		        			// source + target
		        			System.out.print("source: " + "n" + sourceId);
		        			System.out.print("	target: " + "n" + targetId);
		        			
		        			// length
		        			System.out.println("	length:" + currentShortestPath.getLength());
		        			
		        			// segment values
		        			if(currentShortestPath.getLength() != Double.POSITIVE_INFINITY) {
			        			for(int j = 0; j < currentShortestPath.getNumberOfNodes(); j++) {
			        				System.out.println("segment: n" + currentShortestPath.getNode(j));
			        			}
		        			}
		        		}
		        	}
		        }
			}
			
		
			// if user provided -h or --help print help text
		    if (cmd.hasOption("help") || cmd.hasOption('h')) {
		    	 printHelp(options);
		    }
		
		    
		    // if user provided command line arguments without any flag
		    String[] remainder = cmd.getArgs();
		    if(remainder.length > 1) {
		        System.out.println("\nWARNING: Could not assign argument(s) to any option:");
		        for (int i=1; i < remainder.length; i++) {
		            System.out.print(remainder[i]);
		            System.out.print(" ");
		        }
		        System.out.println();
		    }
	    
		} catch (Exception b) {
			System.out.println("ERROR: First argument needs to be .graphml file"
					+ "\n	Provide correct file name with path: /<filename>.graphml"
					+ "\n	Get help using -h or --help");
		}
		
	}
	
	
	// initiate parsing of the graph
	private void parseGraph(String inputFileName) {
		// start parsing the graph
		FileHandler nFileHandler = new FileHandler();
		nFileHandler.setGraphmlFile(claArgs[0]);
		nFileHandler.prepareParser();

		// instantiate Graph object
		graph = new Graph(nFileHandler.getEdgeList(), nFileHandler.getNodeList());
	}
	
	
	// help text of options -h, --help
	void printHelp(Options options) {
		System.out.println("\n---------------------------------------------");
    	String header = "Start parsing by providing input file name:\n" + 
    					"$> java Main <inputfile.graphml>	adding Options:\n";
		String footer = "Options, flags and arguments may be in any order.\n" +
						"---------------------------------------------\n\n";
		
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("The Network Graph Handler", header, options, footer, false);
	}
	
	
} 
	

	