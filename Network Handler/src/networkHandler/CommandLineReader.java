package networkHandler;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.NoSuchElementException;
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
	
	private 				String[] claArgs;
	private final Logger	mylog = LogManager.getLogger(CommandLineReader.class);
	private String 			inputFileName;
	private String 			outputFileName;
	private int 			bcmNodeID;
	private int 			spIDone;
	private int 			spIDtwo;
	private boolean 		flagReadFile;
	private boolean 		flagCreateOutputFile;
	private boolean 		flagBCMSingle;
	private	boolean			flagBCMAll;
	private boolean 		flagConnectivity;
	private boolean 		flagDiameter;
	private boolean 		flagShortestPathsTwoNodes;
	private boolean 		flagShortestPathsAll;
	private	boolean 		flagShortestPathsNoDuplicates;
	private boolean 		flagGraphAttributes;
	private	boolean			flagMinimumSpanningTree;
	
	
	
	/**
	 * This constructs the parser of command line arguments.
	 * Program arguments / command line arguments are passed to this constructor.
	 * @param args The provided command line arguments
	 */
	public CommandLineReader(String[] args) {
		
		this.claArgs						= args;
		this.inputFileName					= "";
		this.outputFileName					= "";
		this.bcmNodeID						= 0;
		this.spIDone						= 0;
		this.spIDtwo						= 0;
		this.flagReadFile					= false;
		this.flagCreateOutputFile			= false;
		this.flagBCMSingle					= false;
		this.flagBCMSingle					= false;
		this.flagConnectivity				= false;
		this.flagDiameter					= false;
		this.flagShortestPathsTwoNodes		= false;
		this.flagShortestPathsAll			= false;
		this.flagShortestPathsNoDuplicates	= false;
		this.flagGraphAttributes			= false;
		this.flagMinimumSpanningTree		= false;
		
	}

	
	
	// getter
	public	String	getInputFileName()					{return inputFileName;}
	public	String	getOutputFileName()					{return outputFileName;}
	public	int		getBcmNodeID()						{return bcmNodeID;}
	public	int		getSpIDone()						{return spIDone;}
	public	int		getSpIDtwo()						{return spIDtwo;}
	public	boolean	getFlagReadFile()					{return flagReadFile;}
	public	boolean	getFlagCreateOutputFile()			{return flagCreateOutputFile;}
	public	boolean	getFlagBCMSingle()					{return flagBCMSingle;}
	public	boolean	getFlagBCMAll()						{return flagBCMAll;}
	public	boolean	getFlagConnectivity()				{return flagConnectivity;}
	public	boolean	getFlagDiameter()					{return flagDiameter;}
	public	boolean	getFlagShortestPathsTwoNodes()		{return flagShortestPathsTwoNodes;}
	public	boolean	getFlagShortestPathsAll()			{return flagShortestPathsAll;}
	public	boolean	getFlagShortestPathsNoDuplicates()	{return flagShortestPathsNoDuplicates;}
	public	boolean	getFlagGraphAttributes()			{return flagGraphAttributes;}
	public	boolean	getFlagMinimumSpanningTree()		{return flagMinimumSpanningTree;}
	public	boolean	getAnyPrintFlag() {
		return (	flagBCMSingle
				||	flagBCMAll
				||	flagConnectivity
				||	flagDiameter
				||	flagShortestPathsTwoNodes
				||	flagShortestPathsAll
				||	flagGraphAttributes
				||	flagMinimumSpanningTree);
	}
	
	
	/**
	 * This method parses the command line arguments by using the external library commons-cli-1.4.
	 * It calls the parsing of the graph and the export of the graph calculations into a new file.
	 * 
	 * This method handles the command line arguments in three stages:
	 * 1. Definition stage: creating the command line options
	 * 2. Parsing stage: creating the parser
	 * 3. Interrogation stage: check whether options available and call methods accordingly 
	 */
	public void doParseCommandLineArguments() {
		
		// 1. Definition stage: creating the command line options
		
		Option option_a = Option.builder("a")
					    		.hasArgs()
					    		.argName("outputfile.graphml")
					    		.argName("duplciates").optionalArg(true)
					    		.desc("Print all graph calculations and node/edge properties to a new *.graphml file. "
					    				+ "\nProvide file name and path."
					    				+ "\nadd 'nodup' to avoid duplicates.")
					    		.build();
	    
		Option option_b = Option.builder("b")
								.hasArg()
								.argName("node_id")
								.desc("Calculate the betweenness centrality measure for a selected node."
										+ "\nProvide a Node id.")
								.build();
		
		Option option_B = Option.builder("B")
								.desc("Calculate the betweenness centrality measure for all nodes.")
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
								.hasArg()
								.optionalArg(true)
								.desc("Calculate all shortest paths between all nodes."
										+ "\nadd 'nodup' to avoid duplicates.")
								.build();
		
		Option option_G = Option.builder("G")
								.longOpt("graph")
								.desc("Print # of nodes and edges.")
								.build();
		
		Option option_t = Option.builder("t")
								.desc("Calculate a minimum spanning tree of the given graph.")
								.build();
		
		Option option_help = Option.builder("h")
								.longOpt("help")
								.desc("Print this help text.")
								.build();
			
		Options options = new Options();
		options.addOption(option_a);
		options.addOption(option_b);
		options.addOption(option_B);
		options.addOption(option_c);
		options.addOption(option_d);
		options.addOption(option_s);
		options.addOption(option_S);
		options.addOption(option_G);
		options.addOption(option_t);
		options.addOption(option_help);
		
		// start creating the parser
		doCreateParser(options);
	}
	
	
	// 2. parsing stage: creating the parser
	private void doCreateParser(Options options) {
		
		try {
			
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd;
			cmd = parser.parse(options, claArgs);
			
			// start interrogation stage
			doInterrogateCommandLineArguments(options, cmd);
			
		} catch (ParseException e) {
			
			mylog.error("You provided (a) incorrect option(s) and/or missing option value(s). " +
					"Use -h or --help to print usage help. \n" + e.getMessage());
			
		}
		
	}
	
	
	// 3. interrogation stage: check whether options available and call methods accordingly
	private void doInterrogateCommandLineArguments(Options options, CommandLine cmd) {	
		
		try {
			
			// if first provided argument is a .graphml file start parsing the graph
			if( claArgs[0].contains(".graphml") ) {
				
				inputFileName = claArgs[0];	// set inputFileName = first provided cla		
				flagReadFile = true;
			
			// if user input is -h or --help print the help text and exit
			} else if ( claArgs[0].contains("help") || ( (claArgs[0].endsWith("h")) && (cmd.hasOption('h')) ) ) {
				
				doPrintHelpText(options);
				System.exit(0);
			
			// if user did not provide proper .graphml file OR provided wrong arguments
			}
		// if user did not provide valid input file as first argument
		} catch (IllegalArgumentException e) {

			mylog.error("First argument needs to be .graphml file. " +
					"Provide correct file name with path: /<filename>.graphml. " +
					"Get help using -h or --help \n" + e.getMessage());

		}
		
		
		// if command line arguments contain -a set outputFileName to given output file name
		// and set flag to call GraphWriter
	    if (cmd.hasOption("a")) {
	    	
	    	outputFileName = cmd.getOptionValue('a');
	    	flagCreateOutputFile = true;
	    	
	    	// if user does not want duplicates in shortest paths calculation to be written in output file
			if ( (cmd.getOptionValues("a").length == 2) && (cmd.getOptionValues("a")[1].equals("nodup")) ) { 
				flagShortestPathsNoDuplicates = true;
//				System.out.println( flagShortestPathsNoDuplicates );
			}
			
	    }
	
	    
	    // if command line arguments contain -b print BCM to sys.out	    
	    if (cmd.hasOption("b")) {
	    	
	    	try {
	    		
	    		// test if value of 'b' is digit
//		    	if( Character.isDigit(cmd.getOptionValue('b').charAt(0)) ) {
		    		
				    try {
				    	
				    	bcmNodeID = Integer.parseInt(cmd.getOptionValue('b'));
				    	flagBCMSingle = true;
				    	
			    	// if user provides non existing Node ID:
					} catch (NoSuchElementException nsee) {

				    	mylog.error("Can not calculate BCM. " +
								"\n The Node ID you provided does not exist." +
								"\n use -G or --graph to display Graph properties" +
								"\n	Use -h or --help to print usage help. \n" +nsee.getMessage() );

					}
				    
//				} else {
//					
//					throw new NumberFormatException(cmd.getOptionValue('b'));
//					
//				}
		    	
	    	} catch (NumberFormatException e) {

	    		mylog.error("Can not calculate BCM." +
						"\n	Wrong number format. " +
						"\n	Use -h or --help to print usage help. \n" +e.getMessage());

	    	}
	    	
	    }
	    
	    
	    // if command line arguments contain -B print ALL BCM to sys.out
	    if (cmd.hasOption('B')) {
	    	
	    	flagBCMAll = true;
	    	
	    }
	    
	    
	    // if command line arguments contain -c print connectivity to sys.out
	    if (cmd.hasOption('c')) {

	    	flagConnectivity = true;
	    	
	    }
	
	    
	    // if command line arguments contain -d print diameter to sys.out
	    if (cmd.hasOption('d')) {

	    	flagDiameter = true;
	    	
	    }
	
	
	    // if command line arguments contain -s calculate shortest path between two given nodes and print to sys.out		    
		if (cmd.hasOption('s')) {
			
			try {
				
			    // test if both values of 's' are digits
//				if( Character.isDigit(cmd.getOptionValues("s")[0].charAt(0))
//			    		&& Character.isDigit(cmd.getOptionValues("s")[1].charAt(0)) ) {
					
					try {	
						
						flagShortestPathsTwoNodes = true;
						spIDone = Integer.parseInt(cmd.getOptionValues("s")[0]);
						spIDtwo = Integer.parseInt(cmd.getOptionValues("s")[1]);
						
					// if user provides non existing Node ID's:
					} catch (NoSuchElementException nsee) {

						mylog.error("Can not calculate shortest path.\n" +
								"The Node ID you provided does not exist.\n" +
								"use -G or --graph to display Graph properties.\n" +
								"Use -h or --help to print usage help. \n " + nsee.getMessage());

					}

				
			} catch (NumberFormatException e) {

				mylog.error("Can not calculate shortest path.\n" +
						"Wrong number format.\n" +
						"Use -h or --help to print usage help. \n" + e.getMessage());

			}
			
		}
	    
		
		// if command line arguments contain -G print graph attributes to sys.out
		if (cmd.hasOption('G')) {

			flagGraphAttributes = true;
			
		}
		
		
		// if command line arguments contain -S calculate ALL shortest paths and print to sys.out
		if (cmd.hasOption('S')) {
			
			flagShortestPathsAll = true;
			
			// if user does not want duplicates in shortest path calculation
			if ( (cmd.getOptionValue('S') != null) && (cmd.getOptionValue('S').equals("nodup")) ) {
				
				flagShortestPathsNoDuplicates = true;
//				System.out.println( flagShortestPathsNoDuplicates );
				
			}

		}
		
		
		// if command line arguments contain -t calculate a spanning tree and print to sys.out
		if (cmd.hasOption('t')) {
			
			flagMinimumSpanningTree = true;
			
		}
	
		
		// if command line arguments contain -h or --help print help text
	    if (cmd.hasOption("help") || cmd.hasOption('h')) {
	    	
	    	 doPrintHelpText(options);
	    	 
	    }
	
	    
	    // if user provided command line arguments without any flag
	    String[] remainder = cmd.getArgs();
	    if(remainder.length > 1) {

	    	mylog.error("Could not assign argument(s) to any option: ");

	        for (int i=1; i < remainder.length; i++) {
	        	
	        	mylog.error(remainder[i]);
	        	mylog.error(" ");

	        }

	    }
	    
	    
	}
	
	
	// help text of options -h, --help output with logger
	private void doPrintHelpText(Options options) {

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

	}
	
	
} 
	
