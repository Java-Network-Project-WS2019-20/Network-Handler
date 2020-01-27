package networkHandler;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Main {

	private static final Logger mylog = LogManager.getLogger(GraphHandler.class);


	public static void main(String[] args) throws NoArgumentException {


		//C:\\h.graphml -a outp.graphml

		BasicConfigurator.configure();

		// user must provide at least one argument = input filename to start parsing graph
//		try {
			if (args.length > 0) {
				
				// start parsing cla
				CommandLineReader commandLineReader = new CommandLineReader(args);
				commandLineReader.claParser();	
				
				if(commandLineReader.getFlagReadFile()) {
					FileHandler	fileHandler = new FileHandler();
					fileHandler.setGraphmlFile(commandLineReader.getInputFileName());
					fileHandler.prepareParser();
					GraphHandler graphHandler = new GraphHandler(fileHandler.getEdgeList(), fileHandler.getNodeList());
					graphHandler.runCalculations(commandLineReader);
				}
			} else {
				mylog.error("Please input valid arguments!");
			} 
//		} catch (NoArgumentException e) {
//			System.out.println("ERROR: Provide at least one argument."
//					+ "\n	Use -h or --help to print usage help.");
//		}
	
		
		
	}
}

