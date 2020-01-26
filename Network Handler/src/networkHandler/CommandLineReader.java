package networkHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.TreeSet;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * This class implements a parser for command line arguments.
 * @author Sebastian Monok
 *
 */
public class CommandLineReader {
	private String[] claArgs;
	private GraphHandler graphHandler;
	private final Logger mylog = LogManager.getLogger(CommandLineReader.class);
	
	private String inputFileName;
	private String outputFileName;
	private int bcmNodeID;
	private int spIDone;
	private int spIDtwo;
	private boolean flagStartParsing;
	private boolean flagOutputFile;
	private boolean flagBCM;
	private boolean flagConnectivity;
	private boolean flagDiameter;
	private boolean flagShortestPathBetweenTwoNodes;
	private boolean flagAllShortestPaths;
	private boolean flagGraphAttributes;
	
	
	
	/**
	 * This constructs the parser of command line arguments.
	 * Program arguments / command line arguments are passed to this constructor.
	 * @param args The provided command line arguments
	 */
	public CommandLineReader(String[] args) {
		this.claArgs = args;
		this.inputFileName = "";
		this.outputFileName = "";
		this.bcmNodeID = 0;
		this.spIDone = 0;
		this.spIDtwo = 0;
		this.flagStartParsing = false;
		this.flagOutputFile = false;
		this.flagBCM = false;
		this.flagConnectivity = false;
		this.flagDiameter = false;
		this.flagShortestPathBetweenTwoNodes = false;
		this.flagAllShortestPaths = false;
		this.flagGraphAttributes = false;
	}

	
	
	// getter
	public String getInputFileName() {return inputFileName;}
	public String getutputFileName() {return outputFileName;}
	public int getBcmNodeID() {return bcmNodeID;}
	public int getSpIDone() {return spIDone;}
	public int getSpIDtwo() {return spIDtwo;}
	public boolean getFlagStartParsing() {return flagStartParsing;}
	public boolean getFlagOutputFile() {return flagOutputFile;}
	public boolean getFlagBCM() {return flagBCM;}
	public boolean getFlagConnectivity() {return flagConnectivity;}
	public boolean getFlagDiameter() {return flagDiameter;}
	public boolean getFlagShortestPathBetweenTwoNodes() {return flagShortestPathBetweenTwoNodes;}
	public boolean getFlagAllShortestPaths() {return flagAllShortestPaths;}
	public boolean getFlagGraphAttributes() {return flagGraphAttributes;}

	
	/**
	 * This method parses the command line arguments by using the external library commons-cli-1.4.
	 * It calls the parsing of the graph and the export of the graph calculations into a new file.
	 * 
	 * This method handles the command line arguments in three stages:
	 * 1. Definition stage: creating the options
	 * 2. Parsing stage: creating the parser
	 * 3. Interrogation stage: check whether options available and call methods accordingly 
	 */
	public void claParser() {
		
		// 1. Definition stage: creating the options
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

			mylog.error("You provided (a) incorrect option(s) and/or missing option value(s). " +
					"Use -h or --help to print usage help.");
//
//	    	System.out.println("ERROR: You provided (a) incorrect option(s) and/or missing option value(s)."
//	    			+ "\n	Use -h or --help to print usage help.");
		}
	}
	
	
	// 3. interrogation stage: check whether options available and call methods accordingly
	private void claParserInterrogation(Options options, CommandLine cmd) {
		
			
		// if first provided argument is .graphml file start parsing the graph
		try { 
			if( claArgs[0].contains(".graphml") ) {
				inputFileName = claArgs[0];	// set inputFileName = first provided cla		
//				parseGraph(inputFileName);	
				
				flagStartParsing = true;
				
			} else if ( claArgs[0].contains("help") || ( (claArgs[0].endsWith("h")) && (cmd.hasOption('h')) ) ) {
				printHelp(options);		// if single argument is -h or --help print help text
				System.exit(0);			// and exit
			} else {
				mylog.error("Wrong file format was provided or wrong arguments were passed!");
				throw new IllegalArgumentException();	// if user did not provide proper .graphml file
			}											// OR provided wrong arguments
		} catch (IllegalArgumentException e) {

			mylog.error("First argument needs to be .graphml file. " +
					"Provide correct file name with path: /<filename>.graphml. " +
					"Get help using -h or --help");

//			System.out.println("ERROR: First argument needs to be .graphml file"
//					+ "\n	Provide correct file name with path: /<filename>.graphml"
//					+ "\n	Get help using -h or --help");
		}
		
		
		// if user provided -a call GraphWriter
	    if (cmd.hasOption("a")) {
//	    	graphHandler.calculateAllGraphProperties();
//	    	GraphWriter gw = new GraphWriter(cmd.getOptionValue('a'), graphHandler);
//	    	gw.exportGraphmlAnalysis();
	    	
	    	outputFileName = cmd.getOptionValue('a');
	    	flagOutputFile = true;
	    }
	
	    
	    // if user provided -b print BCM to sys.out
	    // test if value of 'b' is digit	    
	    if (cmd.hasOption("b")) {
	    	try {
		    	if( Character.isDigit(cmd.getOptionValue('b').charAt(0)) ) {
				    try {
				    	bcmNodeID = Integer.parseInt(cmd.getOptionValue('b'));
//				    	graphHandler.setBetweennessCentralityMeasureParameter(bcmNodeID);
//				    	graphHandler.calculateSingleBetweennessCentralityMeasure();
//				    	System.out.println("Betweenness Centrality Measure of Node n" + bcmNodeID + " is " + graphHandler.getSingleBetweennessCentralityMeasure());
				    	flagBCM = true;
				    	
				    	// if user provides non existing Node ID:
					} catch (NoSuchElementException nsee) {

				    	mylog.error("Can not calculate BCM. " +
								"\n The Node ID you provided does not exist." +
								"\n use -G or --graph to display Graph properties" +
								"\n	Use -h or --help to print usage help." );

//						System.out.println("ERROR: Can not calculate BCM."
//								+ "\n	The Node ID you provided does not exist."
//								+ "\n	use -G or --graph to display Graph properties."
//								+ "\n	Use -h or --help to print usage help.");
					}
				} else {
					throw new NumberFormatException(cmd.getOptionValue('b'));
				}
	    	} catch (NumberFormatException e) {

	    		mylog.error("Can not calculate BCM." +
						"\n	Wrong number format. " +
						"\n	Use -h or --help to print usage help.");

//	    		System.out.println("ERROR: Can not calculate BCM."
//						+ "\n	Wrong number format."
//						+ "\n	Use -h or --help to print usage help.");
	    	}
	    }
	    
		    
	    // if user provided -c print connectivity to sys.out
	    if (cmd.hasOption('c')) {
//	    	graphHandler.calculateConnectivity();
//	        System.out.println("Graph is connected: " + graphHandler.getConnectivity());
	    	
	    	flagConnectivity = true;
	    }
	
	    
	    // if user provided -d print diameter to sys.out
	    if (cmd.hasOption('d')) {
//	    	graphHandler.calculateDiameter();
//	    	System.out.println("Diameter: " + graphHandler.getDiameter());
	    	
	    	flagDiameter = true;
	    }
	
	
	    // if user provided -s calculate shortest path between two given nodes and print to sys.out
	    // test if both values of 's' are digits		    
		if (cmd.hasOption('s')) {
			try {
				if( Character.isDigit(cmd.getOptionValues("s")[0].charAt(0))
			    		&& Character.isDigit(cmd.getOptionValues("s")[1].charAt(0)) ) {
					try {
//						graphHandler.setSingleShortestPathParameters(Integer.parseInt(cmd.getOptionValues("s")[0]), Integer.parseInt(cmd.getOptionValues("s")[1]));
//						graphHandler.calculateSingleShortestPath();
//						System.out.println( "Shortest Path between: "
//								+ cmd.getOptionValues("s")[0] + " and "
//								+ cmd.getOptionValues("s")[1] + " = "
//								+ graphHandler.getSingleShortestPath().getLength());	
						
						flagShortestPathBetweenTwoNodes = true;
						spIDone = Integer.parseInt(cmd.getOptionValues("s")[0]);
						spIDtwo = Integer.parseInt(cmd.getOptionValues("s")[1]);
						
					// if user provides non existing Node ID's:
					} catch (NoSuchElementException nsee) {


						mylog.error("Can not calculate shortest path.\n" +
								"The Node ID you provided does not exist.\n" +
								"use -G or --graph to display Graph properties.\n" +
								"Use -h or --help to print usage help.");

//						System.out.println("ERROR: Can not calculate shortest path."
//								+ "\n	The Node ID you provided does not exist."
//								+ "\n	use -G or --graph to display Graph properties."
//								+ "\n	Use -h or --help to print usage help.");
					}
			    } else {
		        	throw new NumberFormatException(cmd.getOptionValues("s")[0]);
				}
			} catch (NumberFormatException e) {

				mylog.error("Can not calculate shortest path.\n" +
						"Wrong number format.\n" +
						"Use -h or --help to print usage help.");

//				System.out.println("ERROR: Can not calculate shortest path."
//						+ "\n	Wrong number format."
//						+ "\n	Use -h or --help to print usage help.");
			}
		}
	    
		
		// if user provided -G print graph to sys.out
		if (cmd.hasOption('G')) {
//			System.out.println(graphHandler.getGraph());
			
			flagGraphAttributes = true;
		}
		
		
		// if user provided -S calculate all shortest paths and print to sys.out
		if (cmd.hasOption('S')) {
//			graphHandler.calculateAllShortestPaths();
//			TreeSet<Path> shortestPathList = graphHandler.getShortestPathsList();
//			Iterator<Path>	pathIterator = shortestPathList.iterator();
//			while(pathIterator.hasNext()) {
//				Path currentPath = pathIterator.next();
//				//	source + target
//				System.out.print("source: n" + currentPath.getOriginNode());
//				System.out.print(" target: n" + currentPath.getDestinationNode());
//				
//				//	length
//				System.out.println(" length: " + currentPath.getLength());
//				
//				//	segment values
//				if(currentPath.getLength() != Double.POSITIVE_INFINITY) {
//					for(int i = 0; i < currentPath.getNumberOfNodes(); i++) {
//						System.out.println("segment: n" + currentPath.getNode(i));
//					}
//				}
//			}
			
			flagAllShortestPaths = true;	
		}
		
	
		// if user provided -h or --help print help text
	    if (cmd.hasOption("help") || cmd.hasOption('h')) {
	    	 printHelp(options);
	    }
	
	    
	    // if user provided command line arguments without any flag
	    String[] remainder = cmd.getArgs();
	    if(remainder.length > 1) {

	    	mylog.warn("Could not assign argument(s) to any option: ");

//	        System.out.println("\nWARNING: Could not assign argument(s) to any option:");
	        for (int i=1; i < remainder.length; i++) {
	        	mylog.warn(remainder[i]);
	        	mylog.warn(" ");
//	            System.out.print(remainder[i]);
//	            System.out.print(" ");
	        }
//	        System.out.println();
	    }	
	}
	
	
	// initiate parsing of the graph
	private void parseGraph(String inputFileName) {
		// start parsing the graph
		FileHandler nFileHandler = new FileHandler();
		nFileHandler.setGraphmlFile(inputFileName);
		nFileHandler.prepareParser();

		// instantiate Graph object
		Graph graph = new Graph(nFileHandler.getEdgeList(), nFileHandler.getNodeList());
		graphHandler = new GraphHandler(graph);
	}
	
	
	// help text of options -h, --help
	private void printHelp(Options options) {

//		System.out.println("\n---------------------------------------------");
		mylog.info("\n---------------------------------------------");
    	String header = "Start parsing by providing input file name:\n" + 
    					"$> java Main <inputfile.graphml>	adding Options:\n";
		String footer = "Options, flags and arguments may be in any order.\n" +
						"---------------------------------------------\n\n";

		StringWriter out = new StringWriter();
		PrintWriter pw = new PrintWriter(out);

		HelpFormatter formatter = new HelpFormatter();

		formatter.printHelp(pw, 80, "The Network Graph Handler", header, options,
				formatter.getLeftPadding(), formatter.getDescPadding(), footer, false);

		pw.flush();

		mylog.info(out.toString());

//		formatter.printHelp("The Network Graph Handler", header, options, footer, false);


	}
	
	
} 
	

	