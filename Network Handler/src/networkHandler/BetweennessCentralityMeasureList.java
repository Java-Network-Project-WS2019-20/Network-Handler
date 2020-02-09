package networkHandler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;

/**
 * <p>This class is responsible for managing calculations of a single or multiple {@link BetweennessCentralityMeasure}s of a given {@link Graph}.
 * In case of multiple calculations, separate {@link Thread}s are used for efficiency.
 * <p>It handles three different cases of calculation:
 * <p> 1.: All {@link BetweennessCentralityMeasure}s of a given {@link #graph} are calculated.
 * <p> 2.: Only a {@link #singleBetweennessCentralityMeasureValue} for a given {@link #singleCalculationNodeId} is calculated.
 * <p> 3.: A combination of case 1 and 2. First all {@link BetweennessCentralityMeasure}s are calculated and afterwards a {@link #singleBetweennessCentralityMeasureValue} is referenced separately.
 * <p> To differ between these cases multiple constructors are implemented for ease of use.
 * @author Fabian Grun
 * @see BetweennessCentralityMeasure
 * @see GraphProperty
 */

public class BetweennessCentralityMeasureList implements GraphProperty<ArrayList<BetweennessCentralityMeasure>>{
	private Graph graph;
	private ShortestPathList shortestPathsList;
	private ArrayList<BetweennessCentralityMeasure> betweennessCentralityMeasureListValue;
	private BetweennessCentralityMeasure singleBetweennessCentralityMeasureValue;
	private boolean calculateAll;
	private boolean calculateSingle;
	private int singleCalculationNodeId;
	private final Logger mylog = LogManager.getLogger(BetweennessCentralityMeasureList.class);


	/**
	 * Constructor used if it is not necessary to separate a {@link #singleBetweennessCentralityMeasureValue} from the {@link #singleBetweennessCentralityMeasureValue}.
	 * @param graph	The {@link Graph} instance for which the calculations are done
	 * @param shortestPathList An instance of {@link ShortestPathList} containing the information about the shortest {@link Path}s necessary for the calculations
	 */
	public BetweennessCentralityMeasureList(Graph graph, ShortestPathList shortestPathList) {
		this.graph = graph;
		this.shortestPathsList = shortestPathList;
		this.calculateAll = true;
		this.calculateSingle = false;
	}
	
	/**
	 * Constructor used if a {@link #singleBetweennessCentralityMeasureValue} is required.
	 * @param graph The {@link Graph} instance for which the calculations are done
	 * @param shortestPathList An instance of {@link ShortestPathList} containing the information about the shortest {@link Path}s necessary for the calculations
	 * @param calculateAll Boolean indicating whether all {@link BetweennessCentralityMeasure}s have to be calculated
	 * @param singleCalculationNodeId ID identifying the {@link Node} for which a single calculation is required
	 */
	public BetweennessCentralityMeasureList(Graph graph, ShortestPathList shortestPathList, boolean calculateAll, int singleCalculationNodeId) {
		this.graph = graph;
		this.shortestPathsList = shortestPathList;
		this.calculateAll = calculateAll;
		this.calculateSingle = true;
		this.singleCalculationNodeId = singleCalculationNodeId;
	}
	
	/**
	 * {@inheritDoc}
	 *  @return ArrayList of BetweennessCentralityMeasure
	 */
	public ArrayList<BetweennessCentralityMeasure> getValue() {
		return this.betweennessCentralityMeasureListValue;
	}
	
	public BetweennessCentralityMeasure getSingleBetweennessCentralityMeasure() {
		return this.singleBetweennessCentralityMeasureValue;
	}

	/**
	 * <p>{@inheritDoc}
	 * <p>The method first checks whether a full list of {@link BetweennessCentralityMeasure}s needs to be calculated or only a {@link #singleBetweennessCentralityMeasureValue}.
	 * <p>Afterwards it creates and starts separate {@link Thread}s for the separate {@link BetweennessCentralityMeasure}s and waits until they are all finished before terminating.
	 */
	public void run() {
		
		if (this.calculateAll) {
			mylog.debug("Started Calculation for all Betweenness Centrality Measures.");
			this.betweennessCentralityMeasureListValue = new ArrayList<BetweennessCentralityMeasure>();
			ArrayList<Thread> threads = new ArrayList<Thread>();
			for (int i = 0; i < this.graph.getNodeCount(); i++) {
				this.betweennessCentralityMeasureListValue.add(new BetweennessCentralityMeasure(this.graph, this.shortestPathsList, i));
				threads.add(new Thread(this.betweennessCentralityMeasureListValue.get(i), ("BCM Calculation Node " + i)));
				threads.get(i).start();
			}
			for (int i = 0; i < this.graph.getNodeCount(); i++) {
				try {
					threads.get(i).join();
				} catch (InterruptedException e) {
					mylog.error("Something went wrong: " +e.getMessage());
				}
			}
			mylog.debug("Finished Calculation for all Betweenness Centrality Measures.");
			if(this.calculateSingle) {
				this.singleBetweennessCentralityMeasureValue = this.betweennessCentralityMeasureListValue.get(this.singleCalculationNodeId);
			}
		}else {
			mylog.debug("Started Calculation for single Betweenness Centrality Measure.");
			this.singleBetweennessCentralityMeasureValue = new BetweennessCentralityMeasure(this.graph, this.shortestPathsList, this.singleCalculationNodeId);
			Thread thread = new Thread(this.singleBetweennessCentralityMeasureValue, ("BCM Calculation Node " + this.singleCalculationNodeId));
			thread.start();
			try {
				thread.join();
			} catch (InterruptedException e) {
				mylog.error("Something went wrong: " +e.getMessage());
			}
			mylog.debug("Finished Calculation for single Betweenness Centrality Measure.");
		}
		
	}
	
	/**
	 * <p>{@inheritDoc}
	 * <p>Calls the {@link BetweennessCentralityMeasure#printToConsole} method of all managed {@link BetweennessCentralityMeasure}s.
	 */
	public void printToConsole() {
		for(int i = 0; i < this.betweennessCentralityMeasureListValue.size(); i++) {
			this.betweennessCentralityMeasureListValue.get(i).printToConsole();
		}
	}
	
	/**
	 * <p>Calls the {@link BetweennessCentralityMeasure#printToConsole} method of the {@link #singleBetweennessCentralityMeasureValue}.
	 */
	public void printToConsoleSingle() {
		this.singleBetweennessCentralityMeasureValue.printToConsole();
	}
	
	
}
