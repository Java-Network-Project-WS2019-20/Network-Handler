package networkHandler;

public class Main {

	public static void main(String[] args) {

		// cla (command line arguments) for testing:
		
		// C:\\Users\\Krzysztof\\Desktop\\Uni\\Java Project\\Graphen\\medium_graph.graphml -a /outputfile.graphml -b 2 -c -d -s 1 14
		// C:\\Users\\Fabian\\Downloads\\medium_graph.graphml -a /outputfile.graphml -b 2 -c -d -s 1 14
		// C:\\Users\\khali\\OneDrive\\Desktop\\\\gra.xml -b 2 -c -d -s 1 14
		// C:\\Users\\boost\\Downloads\\small_graph.graphml -a /outputfile.graphml -b 2 -c -d -s 1 14
		
		try {
			if (args.length > 0) {	// user must provide at least one argument = input filename to start parsing graph
				
				// start parsing cla
				CommandLineHandler clHandler = new CommandLineHandler(args);
				clHandler.claParser();	
				
			} else {
				throw new Exception();
			} 
		} catch (Exception e) {
			System.out.println("ERROR: Provide at least one argument."
					+ "\n	Use -h or --help to print usage help.");
		}
	
		
		
	}
}

