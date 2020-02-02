package networkHandler;


/**
 * The CommandLineWriter handles all the system outputs
 * @author Fabian Grun
 * @author Sebastian Monok
 */

public class CommandLineWriter implements Runnable{

	private	GraphHandler		graphHandler;
	private	CommandLineReader	commandLineReader;
	
	
	// constructor
	public			CommandLineWriter (GraphHandler graphHandler, CommandLineReader commandLineReader) {
		this.graphHandler		= graphHandler;
		this.commandLineReader	= commandLineReader;
	}
	
	
	/**
	 *  The method run checks which calculations should be printed to sys.out
	 */
	public void		run() {
		
		if(commandLineReader.getFlagGraphAttributes()) {
			graphHandler.printToConsoleGraph();
		}
		
		if(commandLineReader.getFlagConnectivity()) {
			graphHandler.printToConsoleConnectivity();
		}
		
		if(commandLineReader.getFlagDiameter()) {
			graphHandler.printToConsoleDiameter();
		}
		
		if(commandLineReader.getFlagBCMSingle()) {
			graphHandler.printToConsoleBetweennessCentralityMeasureSingle();
		}
		
		if(commandLineReader.getFlagBCMAll()) {
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

