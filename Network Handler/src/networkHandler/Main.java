package networkHandler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * <p> The Main class is responsible for correct initialization of all other classes in the correct Order.
 * <p> It creates instances of {@link CommandLineReader}, {@link GraphmlReader}, {@link GraphHandler}, {@link CommandLineWriter} and {@link GraphmlWriter}
 * and is responsible for communication and forwarding between those classes.
 * @author Fabian Grun
 *
 */
public class Main {

	private static final Logger mylog = LogManager.getLogger(GraphHandler.class);

	/**
	 * <p> The main method creates an instance of {@link CommandLineReader}, forwards the given arguments to it and calls its parsing method.
	 * <p> After parsing the arguments it reads the necessary flags, calls the {@link GraphmlReader} if necessary and forwards the results to an instance of {@link GraphHandler}.
	 * <p> After completing the calculations in {@link GraphHandler}, {@link GraphmlWriter} and {@link CommandLineWriter} are created called as instructed by the results of the {@link CommandLineReader}.
	 * @param args
	 * @throws NoArgumentException
	 */
	public static void main(String[] args) throws NoArgumentException {
		try {
			if (args.length > 0) {
				CommandLineReader commandLineReader = new CommandLineReader(args);
				commandLineReader.doParseCommandLineArguments();	
				if(commandLineReader.getFlagReadFile()) {
					GraphmlReader	graphReader = new GraphmlReader();
					graphReader.setGraphmlFile(commandLineReader.getInputFileName());
					graphReader.prepareParser();
					if (graphReader.isParseSuccessful()) {
						GraphHandler graphHandler = new GraphHandler(graphReader.getEdgeList(), graphReader.getNodeList(), commandLineReader);
						graphHandler.runCalculations();
						Thread graphWriterThread = new Thread();
						Thread commandLineWriterThread = new Thread();
						if (commandLineReader.getFlagCreateOutputFile()) {
							GraphmlWriter graphWriter = new GraphmlWriter(commandLineReader.getInputFileName(), commandLineReader.getOutputFileName(), graphHandler);
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

