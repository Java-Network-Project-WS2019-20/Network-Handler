package networkHandler;


/**
 * <p>This class is responsible for calling printing methods of different {@link GraphProperty}s according to the flags set in a given {@link CommandLineReader}.
 * <p>It implements the {@link Runnable} interface to allow for use of {@link Thread}s. (This is mainly used for parallel console and file output via a {@link GraphWriter}.)
 * @author Fabian Grun
 * @author Sebastian Monok
 * @see GraphHandler
 * @see CommandLineReader
 * @see GraphProperty
 */

public class CommandLineWriter implements Runnable{

	private	GraphHandler graphHandler;
	private	CommandLineReader commandLineReader;
	
	/**
	 * Default Constructor
	 * @param graphHandler
	 * @param commandLineReader
	 */
	public CommandLineWriter (GraphHandler graphHandler, CommandLineReader commandLineReader) {
		this.graphHandler		= graphHandler;
		this.commandLineReader	= commandLineReader;
	}
	
	/**
	 *  <p>The method checks which {@link GraphProperty}s should be printed to the console by checking the flags provided by the given {@link CommandLineReader}.
	 *  <p>If a flag is set, the according printing method of the given {@link GraphHandler} is called. (which further propagates the call to the single {@link GraphProperty}s)
	 */
	public void run() {
		if(commandLineReader.getFlagGraphAttributes()) {
			graphHandler.printToConsoleGraph();
		}
		if(commandLineReader.getFlagConnectivity()) {
			graphHandler.printToConsoleConnectivity();
		}
		if(commandLineReader.getFlagDiameter()) {
			graphHandler.printToConsoleDiameter();
		}
		if(commandLineReader.getFlagBetweennessCentralityMeasureSingle()) {
			graphHandler.printToConsoleBetweennessCentralityMeasureSingle();
		}
		if(commandLineReader.getFlagBetweennessCentralityMeasureAll()) {
			graphHandler.printToConsoleBetweennessCentralityMeasureList();
		}
		if(commandLineReader.getFlagShortestPathsTwoNodes()) {
			graphHandler.printToConsoleShortestPathListTwoNodes();
		}
		if(commandLineReader.getFlagShortestPathsAll()) {
			graphHandler.printToConsoleShortestPathListAll();
		}
		if(commandLineReader.getFlagMinimumSpanningTree()) {
			graphHandler.printToConsoleMinimumSpanningTree();
		}	
	}
}

