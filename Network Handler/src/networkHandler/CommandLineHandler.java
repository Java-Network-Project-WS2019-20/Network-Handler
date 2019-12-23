package networkHandler;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class CommandLineHandler {
	private String[] claArgs;
	private String inputFileName;
	private String outputFileName;
	private boolean bcm;
	private int bcmNodeID;
	private boolean connectivity;
	private boolean diameter;
	private boolean shortestPath;
	private int[] shortestPathNodeIDs;
	private boolean allShortestPaths; 

	
	// constructor
	public CommandLineHandler(String[] args) {
		this.claArgs = args;
		
		this.inputFileName = null;
		this.outputFileName = null;
		this.bcm = false;
		this.bcmNodeID = 0;
		this.connectivity = false;
		this.diameter = false;
		this.shortestPath = false;
		this.shortestPathNodeIDs = new int[] {0, 0};
		this.allShortestPaths = false;
	}
	
	
	// getter ( TODO guess there is no need for setters, or is it better to pass the values to method claParser() with setters? )
	public String[] getClaArgs() { return claArgs; }
	public String getInputFileName() { return inputFileName; }
	public String getOutputFileName() { return outputFileName; }
	public boolean getBcm() { return bcm; }
	public int getBcmNodeID() { return bcmNodeID; }
	public boolean getConnectivity() { return connectivity; }
	public boolean getDiameter() { return diameter; }
	public boolean getShortestPath() { return shortestPath; }
	public int[] getShortestPathNodeIDs() { return shortestPathNodeIDs; }
	public boolean getAllShortestPaths() { return allShortestPaths; }


	// handle the cla (command line arguments) in three stages:
	public void claParser() {
		
		// 1. definition stage: creating the options
		
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
		
		Option option_help = Option.builder("h")
				.longOpt("help")
				.desc("Prints this help text.")
				.build();
			
		Options options = new Options();
		options.addOption(option_a);
		options.addOption(option_b);
		options.addOption(option_c);
		options.addOption(option_d);
		options.addOption(option_s);
		options.addOption(option_S);
		options.addOption(option_help);
		
		
		// 2. parsing stage: creating the parser
	
		try {
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd;
			cmd = parser.parse(options, claArgs);
		
			
		// 3. interrogation stage: check whether options available and passing on
			
			// test if first provided argument is .graphml file
			if( claArgs[0].contains(".graphml") ) {
				//TODO maybe better to check if file exists first
				inputFileName = claArgs[0];	// set inputFileName = first provided cla
			} else if ( claArgs[0].contains("help") || ( (claArgs[0].endsWith("h")) && (cmd.hasOption('h')) ) ) {
				inputFileName = null;
			} else {
				throw new Exception();
			}
		
			
		    if (cmd.hasOption("a")) {
		        outputFileName = cmd.getOptionValue('a');
		    }
		
		    
		    // test if value of 'b' is digit	    
		    if (cmd.hasOption("b")) {
		    	if( Character.isDigit(cmd.getOptionValue('b').charAt(0)) ) {
			        bcm = true; // true = the calculation of bcm should start
			        bcmNodeID = Integer.parseInt(cmd.getOptionValue('b'));;	         
			    } else {
		        	throw new ParseException("argument is not a number.");
				}
		    }
		    
		    
		    if (cmd.hasOption('c')) {
		        connectivity = true; // true = the calculation to check whether connected or not should start
		    }
	
		    
		    if (cmd.hasOption('d')) {
		        diameter = true; // true = the calculation of diameter should start
		    }
		

		    // test if both values of 's' are digits		    
	    	if (cmd.hasOption('s')) {
	    		if( Character.isDigit(cmd.getOptionValues("s")[0].charAt(0))
			    		&& Character.isDigit(cmd.getOptionValues("s")[1].charAt(0)) ) {
			        shortestPath = true;
			        
			        for(int i=0; i<cmd.getOptionValues("s").length; i++) {
			        	shortestPathNodeIDs[i] = Integer.parseInt(cmd.getOptionValues("s")[i]);;
			        }
			        
			    } else {
		        	throw new ParseException("argument is not a number.");
				}
	    	}
		    	
	    	
	    	if (cmd.hasOption('S')) {
	    		allShortestPaths = true;
	    	}
	    	

		    if (cmd.hasOption("help") || cmd.hasOption('h')) {
		    	System.out.println("\n---------------------------------------------");
		    	String header = "Start parsing by providing input file name:\n" + 
		    					"$> java Main <inputfile.graphml>	adding Options:\n";
				String footer = "Options, flags and arguments may be in any order.\n" +
								"---------------------------------------------\n\n";
				
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("The Network Graph Handler", header, options, footer, false); 
		    }
		
		    
		    // if user provided command line arguments without any flag
	        String[] remainder = cmd.getArgs();
	        if(remainder.length > 1) {
		        System.out.print("\nWARNING: Could not assign these arguments to any option: ");
		        for (int i=1; i < remainder.length; i++) {
		            System.out.print(remainder[i]);
		            System.out.print(" ");
		        }
		        System.out.println();
	        }
		    
		    
	    } catch (ParseException e) {
	    	System.out.println("ERROR: You provided (a) incorrect option(s) and/or missing option value(s)."
	    			+ "\nUse -h or --help to print usage help.");
		} catch (Exception b) {
			System.out.println("ERROR: First argument needs to be .graphml file"
					+ "\nProvide correct file name with path: /<filename>.graphml"
					+ "\nGet help using -h or --help");
		}
	    
	
	} // completing claParser()
} // completing class body
	

	