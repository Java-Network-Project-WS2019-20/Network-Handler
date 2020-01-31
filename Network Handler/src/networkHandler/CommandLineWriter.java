package networkHandler;

public class CommandLineWriter implements Runnable{

	private	GraphHandler		graphHandler;
	private	CommandLineReader	commandLineReader;
	
	public	CommandLineWriter(GraphHandler graphHandler, CommandLineReader commandLineReader) {
		this.graphHandler		= graphHandler;
		this.commandLineReader	= commandLineReader;
	}
	
	public	void	run() {
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
