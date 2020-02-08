package networkHandler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Main {

	private static final Logger mylog = LogManager.getLogger(GraphHandler.class);


	public static void main(String[] args) throws NoArgumentException {


		//C:\\h.graphml -a outp.graphml

		//BasicConfigurator.configure();

		// user must provide at least one argument = input filename to start parsing graph
		try {
			if (args.length > 0) {
				
				// start parsing cla
				CommandLineReader commandLineReader = new CommandLineReader(args);
				commandLineReader.doParseCommandLineArguments();	
				
				if(commandLineReader.getFlagReadFile()) {
					GraphReader	graphReader = new GraphReader();
					graphReader.setGraphmlFile(commandLineReader.getInputFileName());
					graphReader.prepareParser();
					
					if (graphReader.isParseSuccessful()) {
						GraphHandler graphHandler = new GraphHandler(graphReader.getEdgeList(), graphReader.getNodeList(), commandLineReader);
						graphHandler.runCalculations();
						Thread graphWriterThread = new Thread();
						Thread commandLineWriterThread = new Thread();
						if (commandLineReader.getFlagCreateOutputFile()) {
							GraphWriter graphWriter = new GraphWriter(commandLineReader.getOutputFileName(), graphHandler);
							graphWriter.doCheckIfFileExists();
							graphWriterThread = new Thread(graphWriter, "File Creation Thread");
							graphWriterThread.start();
						}
						if (commandLineReader.getAnyPrintFlag()) {
							CommandLineWriter commandLineWriter = new CommandLineWriter(graphHandler, commandLineReader);
							commandLineWriterThread = new Thread(commandLineWriter, "Print to Command Line Thread");
							commandLineWriterThread.start();
						}
						try {
							graphWriterThread.join();
							commandLineWriterThread.join();
						} catch (InterruptedException e) {
							mylog.error("Something went wrong: " +e.getMessage());
						}
					}
				}
			} else {
				throw new NoArgumentException();
			}

		} catch (NoArgumentException e) {
			mylog.error("Provide at least one argument."
					+ "\n	Use -h or --help to print usage help.");
		}
	
		
		
	}
}

